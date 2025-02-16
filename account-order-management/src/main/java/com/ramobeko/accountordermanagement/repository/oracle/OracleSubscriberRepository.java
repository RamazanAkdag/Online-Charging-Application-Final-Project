package com.ramobeko.accountordermanagement.repository.oracle;

import com.ramobeko.accountordermanagement.model.shared.OracleSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OracleSubscriberRepository extends JpaRepository<OracleSubscriber, Long> {

}