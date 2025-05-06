package com.ramobeko.accountordermanagement.util.mapper.oracle;

import com.ramobeko.accountordermanagement.model.dto.request.SubscriberRequest;
import com.ramobeko.accountordermanagement.model.dto.request.SubscriberUpdateRequest;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleBalance;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import com.ramobeko.accountordermanagement.model.entity.oracle.OraclePackage;
import com.ramobeko.accountordermanagement.model.shared.OracleSubscriber;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Date;

@Mapper(componentModel = "spring")
public interface OracleSubscriberMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(SubscriberUpdateRequest dto, @MappingTarget OracleSubscriber subscriber);

    // Subscriber oluşturma özel mantık içerdiğinden default method
    default OracleSubscriber create(OracleCustomer customer,
                                    OraclePackage packagePlan,
                                    SubscriberRequest request,
                                    String phoneNumber) {
        OracleSubscriber subscriber = new OracleSubscriber();
        subscriber.setCustomer(customer);
        subscriber.setPackagePlan(packagePlan);
        subscriber.setPhoneNumber(phoneNumber);
        subscriber.setStartDate(request.getStartDate() != null ? request.getStartDate() : new Date());
        subscriber.setStatus("ACTIVE");
        return subscriber;
    }

    default OracleBalance createBalance(OracleSubscriber subscriber, OraclePackage packagePlan) {
        OracleBalance balance = new OracleBalance();
        balance.setSubscriber(subscriber);
        balance.setPackagePlan(packagePlan);
        balance.setLevelMinutes(packagePlan.getAmountMinutes());
        balance.setLevelSms(packagePlan.getAmountSms());
        balance.setLevelData(packagePlan.getAmountData());
        balance.setStartDate(new Date());
        balance.setEndDate(null);
        return balance;
    }
}

