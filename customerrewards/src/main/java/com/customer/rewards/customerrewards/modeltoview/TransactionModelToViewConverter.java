package com.customer.rewards.customerrewards.modeltoview;

import com.customer.rewards.customerrewards.model.CustomerTransaction;
import com.customer.rewards.customerrewards.view.CustomerTransactionDetails;

import org.springframework.stereotype.Component;

@Component
public class TransactionModelToViewConverter {

  CustomerTransactionDetails convertTransactionModelToView(
      final CustomerTransaction customerTransaction) {
    final CustomerTransactionDetails customerTransactionDetails = new CustomerTransactionDetails();
    customerTransactionDetails.setId(customerTransaction.getId());
    customerTransactionDetails.setPaymentType(customerTransaction.getPaymentType());
    customerTransactionDetails.setLast4DigitsOfPayment(
        customerTransaction.getLast4DigitsOfPayment());
    customerTransactionDetails.setTransactionAmount(customerTransaction.getTransactionAmount());
    customerTransactionDetails.setTransactionTime(customerTransaction.getTransactionTime());
    return customerTransactionDetails;
  }
}
