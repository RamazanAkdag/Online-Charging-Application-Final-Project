package org.example.accountbalancemanagementfunction.repository.oracle;

import com.ramobeko.oracle.model.OracleBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OracleBalanceRepository extends JpaRepository<OracleBalance, Long> {

    @Query("SELECT b FROM OracleBalance b WHERE b.subscriber.id = :subscId")
    Optional<OracleBalance> findBalanceBySubscriberId(@Param("subscId") Long subscId);




}
