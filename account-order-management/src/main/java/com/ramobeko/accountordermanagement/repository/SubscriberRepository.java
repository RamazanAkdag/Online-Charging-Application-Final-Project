package com.ramobeko.accountordermanagement.repository;

import com.ramobeko.accountordermanagement.model.entity.oracle.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

    @Procedure(procedureName = "AOM.PKG_SUBSCRIPTION.ADD_SUBSCRIPTION")
    void addSubscription(Long p_cust_id, Long p_pkg_id,
                         java.util.Date p_start_date, java.util.Date p_end_date,
                         Integer p_balance_minutes, Integer p_balance_sms, Integer p_balance_data);

    @Procedure(procedureName = "AOM.PKG_SUBSCRIPTION.GET_ALL_SUBSCRIPTIONS")
    List<Subscriber> getAllSubscriptions();
}