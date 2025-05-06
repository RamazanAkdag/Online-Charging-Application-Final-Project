package com.ramobeko.accountordermanagement.util.mapper.dto;

import com.ramobeko.accountordermanagement.model.dto.OraclePackageDTO;
import com.ramobeko.accountordermanagement.model.entity.oracle.OraclePackage;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface OraclePackageMapper {

    OraclePackage toEntity(OraclePackageDTO dto);

    OraclePackageDTO toDto(OraclePackage entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(OraclePackageDTO dto, @MappingTarget OraclePackage entity);
}
