package com.ramobeko.accountordermanagement.service.abstrct;

import com.ramobeko.accountordermanagement.model.entity.Subscriber;

import java.util.Date;
import java.util.List;

public interface ISubscriberService {

    /**
     * 📌 Adds a new subscription (Calls the Oracle stored procedure)
     *
     * @param customerId ID of the customer subscribing
     * @param packageId ID of the selected subscription package
     * @param startDate Subscription start date
     * @param endDate Subscription end date
     * @param balanceMinutes Initial balance for call minutes
     * @param balanceSms Initial balance for SMS
     * @param balanceData Initial balance for mobile data
     */
    void addSubscription(Long customerId, Long packageId, Date startDate, Date endDate,
                         int balanceMinutes, int balanceSms, int balanceData);

    /**
     * 📌 Retrieves all subscriptions (Calls the Oracle stored procedure)
     *
     * @return List of all subscriptions.
     */
    List<Subscriber> getAllSubscriptions();
}