package com.ramobeko.accountordermanagement.util.mapper.oracle;

import com.ramobeko.accountordermanagement.model.dto.request.SubscriberRequest;
import com.ramobeko.accountordermanagement.model.dto.request.SubscriberUpdateRequest;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleBalance;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import com.ramobeko.accountordermanagement.model.entity.oracle.OraclePackage;
import com.ramobeko.accountordermanagement.model.shared.OracleSubscriber;

import java.util.Date;

public class OracleSubscriberMapper {

    /**
     * OracleSubscriber nesnesini, verilen müşteri, paket, istek ve telefon numarası bilgileriyle oluşturur.
     *
     * @param customer      OracleCustomer nesnesi
     * @param packagePlan   OraclePackage nesnesi (null ise subscriber oluşturulmaz)
     * @param request       SubscriberRequest nesnesi
     * @param phoneNumber   Oluşturulmuş telefon numarası
     * @return Yeni oluşturulan OracleSubscriber nesnesi
     */
    public static OracleSubscriber fromSubscriberRequest(OracleCustomer customer,
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

    /**
     * Verilen subscriber ve paket bilgilerini kullanarak yeni bir OracleBalance nesnesi oluşturur.
     *
     * @param subscriber  Oluşturulan veya mevcut OracleSubscriber nesnesi
     * @param packagePlan OraclePackage nesnesi
     * @return Oluşturulan OracleBalance nesnesi
     */
    public static OracleBalance createBalance(OracleSubscriber subscriber, OraclePackage packagePlan) {
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

    /**
     * Var olan OracleSubscriber nesnesini, verilen SubscriberUpdateRequest bilgileriyle günceller.
     *
     * @param subscriber Var olan OracleSubscriber nesnesi
     * @param request    SubscriberUpdateRequest nesnesi
     * @return Güncellenmiş OracleSubscriber nesnesi
     */
    public static OracleSubscriber updateSubscriber(OracleSubscriber subscriber, SubscriberUpdateRequest request) {
        if (request.getStartDate() != null) {
            subscriber.setStartDate(request.getStartDate());
        }
        subscriber.setEndDate(request.getEndDate());
        subscriber.setStatus(request.getStatus());
        return subscriber;
    }
}

