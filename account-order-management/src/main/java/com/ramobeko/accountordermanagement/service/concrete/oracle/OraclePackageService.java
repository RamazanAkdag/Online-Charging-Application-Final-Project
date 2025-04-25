package com.ramobeko.accountordermanagement.service.concrete.oracle;

import com.ramobeko.accountordermanagement.model.dto.OraclePackageDTO;
import com.ramobeko.accountordermanagement.model.entity.oracle.OraclePackage;
import com.ramobeko.accountordermanagement.repository.oracle.OraclePackageRepository;
import com.ramobeko.accountordermanagement.service.abstrct.oracle.IOraclePackageService;
import com.ramobeko.accountordermanagement.util.mapper.dto.PackageMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OraclePackageService implements IOraclePackageService {

    private final OraclePackageRepository oraclePackageRepository;

    public OraclePackageService(OraclePackageRepository oraclePackageRepository) {
        this.oraclePackageRepository = oraclePackageRepository;
    }

    @Override
    public List<OraclePackage> readAll() {
        return oraclePackageRepository.findAll();
    }

    @Override
    public void create(OraclePackageDTO oraclePackageDTO) {
        OraclePackage oraclePackage = PackageMapper.toEntity(oraclePackageDTO);
        oraclePackageRepository.save(oraclePackage);
    }

    @Override
    public void create(Long id, OraclePackageDTO oraclePackageDTO) {
        // Bu metod ICrudService tarafından zorunlu kılındığı için ama kullanılmayacağı için boş bırakılabilir
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
            existingPackage.setName(oraclePackageDTO.getName());
            existingPackage.setAmountMinutes(oraclePackageDTO.getAmountMinutes());
            existingPackage.setAmountSms(oraclePackageDTO.getAmountSms());
            existingPackage.setAmountData(oraclePackageDTO.getAmountData());
            existingPackage.setPrice(oraclePackageDTO.getPrice());

            oraclePackageRepository.save(existingPackage);
        }
    }

    @Override
    public void delete(Long id) {
        oraclePackageRepository.deleteById(id);
    }
}
