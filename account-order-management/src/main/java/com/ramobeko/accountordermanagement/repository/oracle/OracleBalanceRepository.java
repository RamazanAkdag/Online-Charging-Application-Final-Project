package com.ramobeko.accountordermanagement.repository.oracle;

import com.ramobeko.accountordermanagement.model.entity.oracle.OracleBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OracleBalanceRepository extends JpaRepository<OracleBalance, Long> {
}
