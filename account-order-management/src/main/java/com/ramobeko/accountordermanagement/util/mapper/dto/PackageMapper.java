package com.ramobeko.accountordermanagement.util.mapper.dto;

import com.ramobeko.accountordermanagement.model.dto.OraclePackageDTO;
import com.ramobeko.accountordermanagement.model.entity.oracle.OraclePackage;

public class PackageMapper {

    public static OraclePackageDTO toDTO(OraclePackage oraclePackage) {
        if (oraclePackage == null) return null;
        return new OraclePackageDTO(
                oraclePackage.getId(),
                oraclePackage.getName(),
                oraclePackage.getAmountMinutes(),
                oraclePackage.getAmountSms(),
                oraclePackage.getAmountData(),
                oraclePackage.getPrice()
        );
    }

    public static OraclePackage toEntity(OraclePackageDTO dto) {
        if (dto == null) return null;
        OraclePackage oraclePackage = new OraclePackage();
        oraclePackage.setId(dto.getId());
        oraclePackage.setName(dto.getName());
        oraclePackage.setAmountMinutes(dto.getAmountMinutes());
        oraclePackage.setAmountSms(dto.getAmountSms());
        oraclePackage.setAmountData(dto.getAmountData());
        oraclePackage.setPrice(dto.getPrice());
        return oraclePackage;
    }
}

