package com.customer.rewards.customerrewards.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CustomerTransactionDetails {
  private Long id;
  private String paymentType;
  private String last4DigitsOfPayment;
  private BigDecimal transactionAmount;
  private LocalDateTime transactionTime;
}
