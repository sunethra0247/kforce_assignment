package com.customer.rewards.customerrewards.view;

import java.util.Map;

import lombok.Data;

@Data
public class CustomerRewardDetailsInLastThreeMonths {
  private Long id;
  private String customerName;
  private Map<String, String> rewardPointsDetails;
}
