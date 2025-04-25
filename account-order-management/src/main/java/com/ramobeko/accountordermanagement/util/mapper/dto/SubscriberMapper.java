package com.ramobeko.accountordermanagement.util.mapper.dto;

import com.ramobeko.accountordermanagement.model.dto.request.SubscriberRequest;
import com.ramobeko.accountordermanagement.model.dto.request.SubscriberUpdateRequest;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import com.ramobeko.accountordermanagement.model.entity.oracle.OraclePackage;
import com.ramobeko.accountordermanagement.model.shared.OracleSubscriber;

import java.util.Date;

public class SubscriberMapper {

    public static OracleSubscriber fromRequest(OracleCustomer customer, OraclePackage packagePlan, SubscriberRequest request, String phoneNumber) {
        OracleSubscriber subscriber = new OracleSubscriber();
        subscriber.setCustomer(customer);
        subscriber.setPackagePlan(packagePlan);
        subscriber.setPhoneNumber(phoneNumber);
        subscriber.setStartDate(request.getStartDate() != null ? request.getStartDate() : new Date());
        subscriber.setStatus("ACTIVE");
        return subscriber;
    }

    public static OracleSubscriber update(OracleSubscriber subscriber, SubscriberUpdateRequest request) {
        if (request.getStartDate() != null) subscriber.setStartDate(request.getStartDate());
        subscriber.setEndDate(request.getEndDate());
        subscriber.setStatus(request.getStatus());
        return subscriber;
    }
}

