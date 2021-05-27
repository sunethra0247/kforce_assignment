package com.customer.rewards.customerrewards.model;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@ToString
@Table(name="customer")
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String customerName;

  @OneToMany(mappedBy = "customer", fetch= FetchType.LAZY,cascade = CascadeType.ALL)
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private Set<CustomerTransaction> transactionList;
}
