package com.ramobeko.accountordermanagement.util.mapper.dto;

import com.ramobeko.accountordermanagement.model.dto.OracleCustomerDTO;
import com.ramobeko.accountordermanagement.model.dto.RegisterRequest;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import org.mapstruct.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
@Mapper(componentModel = "spring")
public interface DtoToOracleCustomerMapper {

    OracleCustomer toEntity(OracleCustomerDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(OracleCustomerDTO dto, @MappingTarget OracleCustomer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(request.getPassword()))")
    @Mapping(target = "startDate", expression = "java(new java.util.Date())")
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "role", expression = "java(request.getRole() != null ? request.getRole() : com.ramobeko.accountordermanagement.util.model.Role.USER)")
    OracleCustomer fromRegisterRequest(RegisterRequest request, @Context PasswordEncoder passwordEncoder);

    @AfterMapping
    default void setDefaults(@MappingTarget OracleCustomer customer) {
        if (customer.getStartDate() == null) customer.setStartDate(new Date());
        if (customer.getStatus() == null) customer.setStatus("ACTIVE");
    }
}


