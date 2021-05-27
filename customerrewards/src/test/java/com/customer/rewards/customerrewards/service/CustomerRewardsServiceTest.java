package com.customer.rewards.customerrewards.service;

import com.customer.rewards.customerrewards.exception.CustomerNotFoundException;
import com.customer.rewards.customerrewards.model.Customer;
import com.customer.rewards.customerrewards.model.CustomerTransaction;
import com.customer.rewards.customerrewards.modeltoview.CustomerModelToViewConverter;
import com.customer.rewards.customerrewards.repository.CustomerRepository;
import com.customer.rewards.customerrewards.view.CustomerRewardDetailsInLastThreeMonths;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerRewardsServiceTest {
  private CustomerRepository customerRepository;
  private CustomerRewardsService customerRewardsService;
  private CustomerModelToViewConverter customerModelToViewConverter;

  @Before
  public void setUp() {
    customerRepository = mock(CustomerRepository.class);
    customerModelToViewConverter = mock(CustomerModelToViewConverter.class);
    customerRewardsService =
        new CustomerRewardsService(customerRepository, customerModelToViewConverter);
  }

  @Test(expected = CustomerNotFoundException.class)
  public void shouldThrowUserNotFoundExceptionIfCustomerIsNotExist() {
    when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());
    customerRewardsService.getCustomerDetails(anyLong());
  }

  @Test
  public void shouldReturnCustomerIfExists() {
    final Customer customer = mock(Customer.class);
    when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
    customerRewardsService.getCustomerDetails(anyLong());
    verify(customerModelToViewConverter, times(1)).convertCustomerModelToView(any(), anyLong());
  }

  @Test
  public void shouldCalculateConsecutiveThreeMonthsRewardPoints() {
    final Customer customer = new Customer();
    customer.setId(1L);
    customer.setCustomerName("JohnSnow");
    final CustomerTransaction customerTransaction = new CustomerTransaction();
    customerTransaction.setId(1L);
    customerTransaction.setCustomer(customer);
    customerTransaction.setTransactionTime(LocalDateTime.now());
    customerTransaction.setTransactionAmount(BigDecimal.valueOf(100));
    customerTransaction.setPaymentType("AMEX");
    customerTransaction.setLast4DigitsOfPayment("XXX5478");
    final CustomerTransaction customerTransaction1 = new CustomerTransaction();
    customerTransaction1.setId(1L);
    customerTransaction1.setCustomer(customer);
    customerTransaction1.setTransactionTime(LocalDateTime.now().minusDays(32));
    customerTransaction1.setTransactionAmount(BigDecimal.valueOf(80));
    customerTransaction1.setPaymentType("AMEX");
    customerTransaction1.setLast4DigitsOfPayment("XXX5478");
    final CustomerTransaction customerTransaction2 = new CustomerTransaction();
    customerTransaction2.setId(1L);
    customerTransaction2.setCustomer(customer);
    customerTransaction2.setTransactionTime(LocalDateTime.now().minusDays(65));
    customerTransaction2.setTransactionAmount(BigDecimal.valueOf(120));
    customerTransaction2.setPaymentType("AMEX");
    customerTransaction2.setLast4DigitsOfPayment("XXX5478");
    final CustomerTransaction customerTransaction3 = new CustomerTransaction();
    customerTransaction3.setId(1L);
    customerTransaction3.setCustomer(customer);
    customerTransaction3.setTransactionTime(LocalDateTime.now().minusDays(95));
    customerTransaction3.setTransactionAmount(BigDecimal.valueOf(120));
    customerTransaction3.setPaymentType("AMEX");
    customerTransaction3.setLast4DigitsOfPayment("XXX5478");
    final Set<CustomerTransaction> customerTransactionsList =
        Stream.of(
                customerTransaction,
                customerTransaction1,
                customerTransaction2,
                customerTransaction3)
            .collect(Collectors.toCollection(HashSet::new));

    customer.setTransactionList(customerTransactionsList);
    when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer));
    final List<CustomerRewardDetailsInLastThreeMonths> result =
        customerRewardsService.getCustomerRewardDetailsInLastThreeMonths(3L);
    Assert.assertNotNull(result);
    Assert.assertEquals(1, result.size());
    final Map<String, String> rewaardPointDetails = result.get(0).getRewardPointsDetails();
    Assert.assertTrue(
        rewaardPointDetails.containsValue("After third month total reward points 170"));
  }
}
