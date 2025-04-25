package com.ramobeko.accountordermanagement.repository.oracle;

import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OracleCustomerRepository extends JpaRepository<OracleCustomer, Long> {
    Optional<OracleCustomer> findByEmail(String email);

    boolean existsByEmail(String email);

    OracleCustomer findByResetToken(String token);
}