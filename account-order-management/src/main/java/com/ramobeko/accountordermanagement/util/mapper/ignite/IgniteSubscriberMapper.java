package com.ramobeko.accountordermanagement.util.mapper.ignite;

import com.ramobeko.accountordermanagement.model.shared.OracleSubscriber;
import com.ramobeko.ignite.IgniteBalance;
import com.ramobeko.ignite.IgniteSubscriber;

import java.util.Date;
import java.util.stream.Collectors;

public class IgniteSubscriberMapper {

    public static IgniteSubscriber fromOracle(OracleSubscriber oracleSubscriber) {
        if (oracleSubscriber == null) {
            throw new IllegalArgumentException("OracleSubscriber cannot be null");
        }

        return new IgniteSubscriber(
                oracleSubscriber.getId(),
                oracleSubscriber.getCustomer() != null ? oracleSubscriber.getCustomer().getId() : null,
                oracleSubscriber.getPackagePlan() != null ? oracleSubscriber.getPackagePlan().getId() : null,
                oracleSubscriber.getPhoneNumber(),
                oracleSubscriber.getStartDate() != null ? oracleSubscriber.getStartDate() : new Date(),
                oracleSubscriber.getEndDate(),
                oracleSubscriber.getStatus(),
                oracleSubscriber.getBalances().stream()
                        .map(balance -> new IgniteBalance(
                                balance.getId(),
                                balance.getSubscriber() != null ? balance.getSubscriber().getId() : null,
                                balance.getPackagePlan() != null ? balance.getPackagePlan().getId() : null,
                                balance.getLevelMinutes(),
                                balance.getLevelSms(),
                                balance.getLevelData(),
                                balance.getStartDate(),
                                balance.getEndDate()
                        ))
                        .collect(Collectors.toList())
        );
    }
}

