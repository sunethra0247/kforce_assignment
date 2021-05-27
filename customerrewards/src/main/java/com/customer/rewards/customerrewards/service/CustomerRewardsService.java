package com.customer.rewards.customerrewards.service;

import com.customer.rewards.customerrewards.exception.CustomerNotFoundException;
import com.customer.rewards.customerrewards.model.Customer;
import com.customer.rewards.customerrewards.model.CustomerTransaction;
import com.customer.rewards.customerrewards.modeltoview.CustomerModelToViewConverter;
import com.customer.rewards.customerrewards.repository.CustomerRepository;
import com.customer.rewards.customerrewards.view.CustomerRewardDetailsInLastThreeMonths;
import com.customer.rewards.customerrewards.view.CustomerRewards;
import com.customer.rewards.customerrewards.view.CustomersList;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import static com.customer.rewards.customerrewards.utils.ApplicationConstants.HIGHER_CUT_OFF_AMOUNT_FOR_REWARDS;
import static com.customer.rewards.customerrewards.utils.ApplicationConstants.LOWER_CUT_OFF_AMOUNT_FOR_REWARDS;

@Service
@RequiredArgsConstructor
public class CustomerRewardsService {

  private final CustomerRepository customerRepository;
  private final CustomerModelToViewConverter customerModelToViewConverter;

  public CustomerRewards getCustomerDetails(final Long id) {
    final Optional<Customer> customer = customerRepository.findById(id);
    if (customer.isEmpty()) {
      throw new CustomerNotFoundException();
    }

    return customerModelToViewConverter.convertCustomerModelToView(
        customer.get(),
        customer.get().getTransactionList().stream()
            .filter(
                customerTransaction ->
                    customerTransaction.getTransactionAmount().compareTo(BigDecimal.valueOf(50L))
                        > 0)
            .map(this::calculateRewardPointsForGivenTransaction)
            .reduce(0L, Long::sum));
  }

  public CustomersList getAllCustomersDetails() {
    final List<Customer> customers = customerRepository.findAll();
    if (customers.isEmpty()) {
      return null;
    }
    final CustomersList customersList = new CustomersList();

    customersList.setCustomerRewardsList(
        customers.stream()
            .map(
                customer ->
                    customerModelToViewConverter.convertCustomerModelToView(
                        customer,
                        customer.getTransactionList().stream()
                            .filter(
                                customerTransaction ->
                                    customerTransaction
                                            .getTransactionAmount()
                                            .compareTo(BigDecimal.valueOf(50L))
                                        > 0)
                            .map(this::calculateRewardPointsForGivenTransaction)
                            .reduce(0L, Long::sum)))
            .collect(Collectors.toList()));
    return customersList;
  }

  public List<CustomerRewardDetailsInLastThreeMonths> getCustomerRewardDetailsInLastThreeMonths(
      final Long months) {
    return customerRepository.findAll().stream()
        .map(customer -> populateLastThreeMonthsDetails(customer, months))
        .collect(Collectors.toList());
  }

  private CustomerRewardDetailsInLastThreeMonths populateLastThreeMonthsDetails(
      final Customer customer, final Long months) {

    final CustomerRewardDetailsInLastThreeMonths customerRewardDetailsInLastThreeMonths =
        new CustomerRewardDetailsInLastThreeMonths();
    customerRewardDetailsInLastThreeMonths.setId(customer.getId());
    customerRewardDetailsInLastThreeMonths.setCustomerName(customer.getCustomerName());

    // using LinkedHashMap to preserve insertion order as we need output in certain order
    final Map<String, String> monthlyRewardsMaps = new LinkedHashMap<>();

    // passing months from end point helps us to find out any consecutive 3months period reward
    // points
    monthlyRewardsMaps.put(
        " first month reward points "
            + calculateRewardPointsInAGiverPeriod(
                customer.getTransactionList(), (months * 30) + 1, ((months - 1) * 30) + 1),
        "After first month total reward points "
            + calculateRewardPointsInAGiverPeriod(
                customer.getTransactionList(), (months * 30) + 1, ((months - 1) * 30) + 1));
    monthlyRewardsMaps.put(
        " second month reward points "
            + calculateRewardPointsInAGiverPeriod(
                customer.getTransactionList(), ((months - 1) * 30) + 1, ((months - 2) * 30) + 1),
        "After second month total reward points "
            + calculateRewardPointsInAGiverPeriod(
                customer.getTransactionList(), (months * 30) + 1, ((months - 2) * 30) + 1));
    monthlyRewardsMaps.put(
        " Third month reward points "
            + calculateRewardPointsInAGiverPeriod(
                customer.getTransactionList(), ((months - 2) * 30) + 1, ((months - 3) * 30)),
        "After third month total reward points "
            + calculateRewardPointsInAGiverPeriod(
                customer.getTransactionList(), (months * 30) + 1, ((months - 3) * 30)));
    customerRewardDetailsInLastThreeMonths.setRewardPointsDetails(monthlyRewardsMaps);

    return customerRewardDetailsInLastThreeMonths;
  }

  private Long calculateRewardPointsInAGiverPeriod(
      final Set<CustomerTransaction> transactionList, final Long firstDay, final Long lastDay) {
    return transactionList.stream()
        .filter(
            t ->
                (t.getTransactionTime().isAfter(LocalDateTime.now().minusDays(firstDay))
                    && t.getTransactionTime().isBefore(LocalDateTime.now().minusDays(lastDay))))
        .filter(
            customerTransaction ->
                customerTransaction.getTransactionAmount().compareTo(BigDecimal.valueOf(50L)) > 0)
        .map(this::calculateRewardPointsForGivenTransaction)
        .reduce(0L, Long::sum);
  }

  private Long calculateRewardPointsForGivenTransaction(
      final CustomerTransaction customerTransaction) {
    final BigDecimal transactionAmount = customerTransaction.getTransactionAmount();
    if (transactionAmount.compareTo(BigDecimal.valueOf(100L)) < 0) {
      return transactionAmount.subtract(LOWER_CUT_OFF_AMOUNT_FOR_REWARDS).longValue();
    } else {
      return transactionAmount.subtract(HIGHER_CUT_OFF_AMOUNT_FOR_REWARDS).longValue() * 2
          + LOWER_CUT_OFF_AMOUNT_FOR_REWARDS.longValue();
    }
  }
}
