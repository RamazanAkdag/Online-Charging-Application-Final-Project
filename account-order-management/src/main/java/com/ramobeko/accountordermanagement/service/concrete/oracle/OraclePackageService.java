package com.ramobeko.accountordermanagement.service.concrete.oracle;


import com.ramobeko.accountordermanagement.model.dto.OraclePackageDTO;

import com.ramobeko.accountordermanagement.model.entity.oracle.OraclePackage;
import com.ramobeko.accountordermanagement.repository.oracle.OraclePackageRepository;
import com.ramobeko.accountordermanagement.service.abstrct.oracle.IOraclePackageService;
import com.ramobeko.accountordermanagement.util.mapper.dto.OraclePackageMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class OraclePackageService implements IOraclePackageService {

    private final OraclePackageRepository oraclePackageRepository;
    private final OraclePackageMapper oraclePackageMapper;

    public OraclePackageService(OraclePackageRepository oraclePackageRepository,
                                OraclePackageMapper oraclePackageMapper) {
        this.oraclePackageRepository = oraclePackageRepository;
        this.oraclePackageMapper = oraclePackageMapper;
    }

    @Override
    public List<OraclePackage> readAll() {
        return oraclePackageRepository.findAll();
    }

    @Override
    public void create(OraclePackageDTO oraclePackageDTO) {
        OraclePackage oraclePackage = oraclePackageMapper.toEntity(oraclePackageDTO);
        oraclePackageRepository.save(oraclePackage);
    }

    @Override
    public void create(Long id, OraclePackageDTO oraclePackageDTO) {
        throw new UnsupportedOperationException("Bu metot kullanılmamalıdır.");
    }

    @Override
    public OraclePackage readById(Long id) {
        return oraclePackageRepository.findById(id).orElse(null);
    }

    @Override
    public void update(OraclePackageDTO oraclePackageDTO) {
        Optional<OraclePackage> existingPackageOpt = oraclePackageRepository.findById(oraclePackageDTO.getId());

        if (existingPackageOpt.isPresent()) {
            OraclePackage existingPackage = existingPackageOpt.get();

            // Mapper üzerinden güncelleme
            oraclePackageMapper.updateFromDto(oraclePackageDTO, existingPackage);

            oraclePackageRepository.save(existingPackage);
        }
    }


    @Override
    public void delete(Long id) {
        oraclePackageRepository.deleteById(id);
    }
}
