package com.ramobeko.accountordermanagement.service;

import com.ramobeko.accountordermanagement.model.entity.Subscriber;
import com.ramobeko.accountordermanagement.repository.SubscriberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class SubscriberService implements ISubscriberService {

    private final SubscriberRepository subscriberRepository;

    public SubscriberService(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

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
    @Transactional
    @Override
    public void addSubscription(Long customerId, Long packageId, Date startDate, Date endDate,
                                int balanceMinutes, int balanceSms, int balanceData) {
        subscriberRepository.addSubscription(customerId, packageId, startDate, endDate, balanceMinutes, balanceSms, balanceData);
    }

    /**
     * 📌 Retrieves all subscriptions (Calls the Oracle stored procedure)
     *
     * @return List of all subscriptions.
     */
    @Override
    public List<Subscriber> getAllSubscriptions() {
        return subscriberRepository.getAllSubscriptions();
    }
}