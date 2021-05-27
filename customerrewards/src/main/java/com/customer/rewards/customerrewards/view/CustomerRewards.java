package com.customer.rewards.customerrewards.view;

import java.util.Set;

import lombok.Data;

@Data
public class CustomerRewards {

  private Long customerId;
  private String customerName;
  private Set<CustomerTransactionDetails> transactionDetails;
  private Long rewardPoints;
}
