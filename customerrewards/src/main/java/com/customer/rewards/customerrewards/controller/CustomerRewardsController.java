package com.customer.rewards.customerrewards.controller;

import com.customer.rewards.customerrewards.service.CustomerRewardsService;
import com.customer.rewards.customerrewards.view.CustomerRewardDetailsInLastThreeMonths;
import com.customer.rewards.customerrewards.view.CustomerRewards;
import com.customer.rewards.customerrewards.view.CustomersList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class CustomerRewardsController {

  private final CustomerRewardsService customerRewardsService;

  @GetMapping(path = "/{id}")
  @Operation(summary = "Get One Customer Details")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Customer found"),
        @ApiResponse(responseCode = "400", description = "Bad Request, minimum id should be 1"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
      })
  public ResponseEntity<CustomerRewards> getCustomerDetails(@PathVariable final Long id) {
    if (id < 1) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    return ResponseEntity.ok(customerRewardsService.getCustomerDetails(id));
  }

  @GetMapping(path = "/all")
  @Operation(summary = "Get All Customer Details Along with Transactions and Rewards")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Customers found"),
        @ApiResponse(responseCode = "404", description = "Customers not found")
      })
  public CustomersList getAllCustomersDetails() {
    return customerRewardsService.getAllCustomersDetails();
  }

  @GetMapping(path = "/rewardDetails")
  @Operation(summary = "Get All Customers Reward Details in any consecutive 3 months")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Customers found"),
        @ApiResponse(responseCode = "400", description = "Bad Request, minimum months should be 3"),
        @ApiResponse(responseCode = "404", description = "Customers not found")
      })
  public ResponseEntity<List<CustomerRewardDetailsInLastThreeMonths>>
      getAllCustomersRewardDetailsInLastThreeMonths(
          @RequestParam(value = "months", defaultValue = "3") final Long months) {
    if (months < 3) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    return ResponseEntity.ok(
        customerRewardsService.getCustomerRewardDetailsInLastThreeMonths(months));
  }
}
