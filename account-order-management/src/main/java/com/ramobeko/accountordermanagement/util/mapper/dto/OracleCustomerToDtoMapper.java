package com.ramobeko.accountordermanagement.util.mapper.dto;

import com.ramobeko.accountordermanagement.model.dto.OracleCustomerDTO;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OracleCustomerToDtoMapper {

    OracleCustomerDTO toDto(OracleCustomer customer);
}

