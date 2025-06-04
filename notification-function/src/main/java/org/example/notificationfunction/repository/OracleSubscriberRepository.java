package org.example.notificationfunction.repository;

import com.ramobeko.oracle.model.OracleSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OracleSubscriberRepository extends JpaRepository<OracleSubscriber, Long> {
    Optional<OracleSubscriber> findByPhoneNumber(String phoneNumber);
}
