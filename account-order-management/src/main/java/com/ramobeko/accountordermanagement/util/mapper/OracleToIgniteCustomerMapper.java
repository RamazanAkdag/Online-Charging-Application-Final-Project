package com.ramobeko.accountordermanagement.util.mapper;

import com.ramobeko.accountordermanagement.model.entity.ignite.IgniteCustomer;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;

public class OracleToIgniteCustomerMapper {

    public static IgniteCustomer map(OracleCustomer customer) {
        IgniteCustomer igniteCustomer = new IgniteCustomer();
        igniteCustomer.setId(customer.getId());
        igniteCustomer.setName(customer.getName());
        igniteCustomer.setRole(customer.getRole());
        igniteCustomer.setEmail(customer.getEmail());
        igniteCustomer.setStartDate(customer.getStartDate());
        igniteCustomer.setStatus(customer.getStatus());
        igniteCustomer.setAddress(customer.getAddress());
        return igniteCustomer;
    }
}

