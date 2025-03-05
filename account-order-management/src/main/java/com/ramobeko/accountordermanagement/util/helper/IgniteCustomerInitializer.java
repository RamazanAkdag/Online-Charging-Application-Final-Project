package com.ramobeko.accountordermanagement.util.factory;

import com.ramobeko.accountordermanagement.model.entity.ignite.IgniteCustomer;
import com.ramobeko.accountordermanagement.util.model.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class IgniteCustomerInitializer {

    private final PasswordEncoder passwordEncoder;

    public IgniteCustomerInitializer(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public IgniteCustomer initializeCustomer(IgniteCustomer customer) {
        customer.setId(Optional.ofNullable(customer.getId()).orElse(System.currentTimeMillis()));
        customer.setStatus(Optional.ofNullable(customer.getStatus()).orElse("ACTIVE"));
        customer.setStartDate(Optional.ofNullable(customer.getStartDate()).orElse(new Date()));
        customer.setRole(Optional.ofNullable(customer.getRole()).orElse(Role.USER));
        if (customer.getPassword() != null) {
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        }
        return customer;
    }
}
