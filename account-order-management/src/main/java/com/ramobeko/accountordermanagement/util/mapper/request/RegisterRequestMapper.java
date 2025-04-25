package com.ramobeko.accountordermanagement.util.mapper.request;

import com.ramobeko.accountordermanagement.model.dto.RegisterRequest;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import com.ramobeko.accountordermanagement.util.model.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

public class RegisterRequestMapper {

    public static OracleCustomer toEntity(RegisterRequest request, PasswordEncoder passwordEncoder) {
        OracleCustomer customer = new OracleCustomer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setStartDate(new Date());
        customer.setStatus("ACTIVE");
        customer.setRole(request.getRole() != null ? request.getRole() : Role.USER);
        return customer;
    }
}
