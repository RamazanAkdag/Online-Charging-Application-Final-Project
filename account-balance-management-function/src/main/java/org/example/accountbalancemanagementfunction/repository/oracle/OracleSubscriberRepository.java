package org.example.accountbalancemanagementfunction.repository.oracle;

import org.example.accountbalancemanagementfunction.model.oracle.OracleSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OracleSubscriberRepository extends JpaRepository<OracleSubscriber, Long> {
    Optional<OracleSubscriber> findByPhoneNumber(String phoneNumber);
}
