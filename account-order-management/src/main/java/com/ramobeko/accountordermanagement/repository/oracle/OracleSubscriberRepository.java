package com.ramobeko.accountordermanagement.repository.oracle;

import com.ramobeko.accountordermanagement.model.shared.OracleSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OracleSubscriberRepository extends JpaRepository<OracleSubscriber, Long> {
    List<OracleSubscriber> findByCustomer_Id(Long userId);

    List<OracleSubscriber> findByCustomerId(Long customerId);
}
