package org.example.charginggatewayfunction.Repository;

import org.example.charginggatewayfunction.model.oracle.PersonalUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OraclePersonalUsageRepository extends JpaRepository<PersonalUsage,Long> {

}
