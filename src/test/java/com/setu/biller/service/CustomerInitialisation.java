package com.setu.biller.service;

import com.setu.biller.entities.Customer;
import lombok.Data;

@Data
public class CustomerInitialisation {
    private static Customer customer;

    public static Customer populateInitialCustomer() {
        customer = new Customer();
        customer.setMobileNumber("7003044507");
        customer.setDueAmount("8000");
        customer.setCustomerName("Clark Kent");
        customer.setRefID("SuperMan123");
        customer.setDueDate("2020-07-22");
        return customer;
    }

}
