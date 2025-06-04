package org.example.charginggatewayfunction.repository;

import com.ramobeko.oracle.model.PersonalUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OraclePersonalUsageRepository extends JpaRepository<PersonalUsage, Long> {
}
