package com.ramobeko.accountordermanagement.repository.oracle;

import com.ramobeko.accountordermanagement.model.entity.oracle.OraclePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OraclePackageRepository extends JpaRepository<OraclePackage, Long> {
}
