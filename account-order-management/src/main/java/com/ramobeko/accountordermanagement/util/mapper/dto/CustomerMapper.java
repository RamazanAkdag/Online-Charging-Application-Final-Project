package com.ramobeko.accountordermanagement.util.mapper.dto;

import com.ramobeko.accountordermanagement.model.dto.OracleCustomerDTO;
import com.ramobeko.accountordermanagement.model.dto.RegisterRequest;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import com.ramobeko.accountordermanagement.util.model.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

public class CustomerMapper {

    public static OracleCustomer fromRegisterRequest(RegisterRequest request, PasswordEncoder passwordEncoder) {
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

    public static OracleCustomer updateFromDTO(OracleCustomerDTO dto, OracleCustomer existing) {
        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getAddress() != null) existing.setAddress(dto.getAddress());
        if (dto.getStatus() != null) existing.setStatus(dto.getStatus());
        return existing;
    }

    public static OracleCustomerDTO toDTO(OracleCustomer customer) {
        OracleCustomerDTO dto = new OracleCustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setAddress(customer.getAddress());
        dto.setStatus(customer.getStatus());
        return dto;
    }
}
