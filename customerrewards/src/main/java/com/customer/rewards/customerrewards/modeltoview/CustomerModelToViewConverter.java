package com.customer.rewards.customerrewards.modeltoview;

import com.customer.rewards.customerrewards.model.Customer;
import com.customer.rewards.customerrewards.view.CustomerRewards;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerModelToViewConverter {

  private final TransactionModelToViewConverter transactionModelToViewConverter;

  public CustomerRewards convertCustomerModelToView(
      final Customer customer, final Long rewardPoints) {
    final CustomerRewards customerRewards = new CustomerRewards();
    customerRewards.setCustomerId(customer.getId());
    customerRewards.setCustomerName(customer.getCustomerName());
    customerRewards.setRewardPoints(rewardPoints);
    customerRewards.setTransactionDetails(
        customer.getTransactionList().stream()
            .map(transactionModelToViewConverter::convertTransactionModelToView)
            .collect(Collectors.toSet()));
    return customerRewards;
  }
}
