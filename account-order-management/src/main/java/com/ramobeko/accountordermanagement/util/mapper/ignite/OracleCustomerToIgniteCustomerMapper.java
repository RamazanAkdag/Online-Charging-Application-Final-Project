package com.ramobeko.accountordermanagement.util.mapper.ignite;

import com.ramobeko.accountordermanagement.model.entity.ignite.IgniteCustomer;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OracleCustomerToIgniteCustomerMapper {

    IgniteCustomer toIgnite(OracleCustomer customer);
}

