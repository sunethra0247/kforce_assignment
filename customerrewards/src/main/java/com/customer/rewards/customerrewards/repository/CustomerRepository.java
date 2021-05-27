package com.customer.rewards.customerrewards.repository;

import com.customer.rewards.customerrewards.model.Customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {}
