package com.ramobeko.accountordermanagement.util.mapper.ignite;

import com.ramobeko.accountordermanagement.model.shared.OracleSubscriber;
import com.ramobeko.ignite.IgniteBalance;
import com.ramobeko.ignite.IgniteSubscriber;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Date;  // 🟢 EKLENDİ
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OracleSubscriberToIgniteSubscriberMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "packagePlanId", source = "packagePlan.id")
    @Mapping(target = "balances", ignore = true)
    IgniteSubscriber toIgnite(OracleSubscriber oracleSubscriber);

    default IgniteSubscriber toIgniteWithBalances(OracleSubscriber oracleSubscriber) {
        IgniteSubscriber igniteSubscriber = toIgnite(oracleSubscriber);
        List<IgniteBalance> igniteBalances = oracleSubscriber.getBalances().stream()
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
                .collect(Collectors.toList());
        igniteSubscriber.setBalances(igniteBalances);
        return igniteSubscriber;
    }

    @AfterMapping
    default void setDefaultStartDate(@MappingTarget IgniteSubscriber subscriber) {
        if (subscriber.getStartDate() == null) {
            subscriber.setStartDate(new Date());
        }
    }
}
