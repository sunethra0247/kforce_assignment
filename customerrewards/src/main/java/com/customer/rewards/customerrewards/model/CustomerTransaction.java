package com.customer.rewards.customerrewards.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "customer_transaction")
public class CustomerTransaction {

  @Id @GeneratedValue private Long id;
  private String paymentType;
  private String last4DigitsOfPayment;
  private BigDecimal transactionAmount;
  private LocalDateTime transactionTime;
  @ManyToOne private Customer customer;
}
