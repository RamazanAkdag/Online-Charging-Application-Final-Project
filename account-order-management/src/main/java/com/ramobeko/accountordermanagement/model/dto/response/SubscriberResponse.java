package com.ramobeko.accountordermanagement.model.dto.response;


import com.ramobeko.accountordermanagement.model.dto.IDTO;
import com.ramobeko.accountordermanagement.model.entity.oracle.OraclePackage;
import com.ramobeko.accountordermanagement.model.shared.OracleSubscriber;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SubscriberResponse implements IDTO {

    private Long id;
    private Long customerId;
    private OraclePackage packagePlan;
    private String phoneNumber;
    private Date startDate;
    private Date endDate;
    private String status;
    private List<BalanceResponse> balances;


    public SubscriberResponse() {}

    public SubscriberResponse(OracleSubscriber subscriber) {
        this.id = subscriber.getId();
        this.customerId = subscriber.getCustomer().getId();
        this.packagePlan = subscriber.getPackagePlan();
        this.phoneNumber = subscriber.getPhoneNumber();
        this.startDate = subscriber.getStartDate();
        this.endDate = subscriber.getEndDate();
        this.status = subscriber.getStatus();
        this.balances = subscriber.getBalances() != null ?
                subscriber.getBalances().stream().map(BalanceResponse::new).collect(Collectors.toList()) : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public OraclePackage getPackagePlan() {
        return packagePlan;
    }

    public void setPackagePlan(OraclePackage packagePlan) {
        this.packagePlan = packagePlan;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BalanceResponse> getBalances() {
        return balances;
    }

    public void setBalances(List<BalanceResponse> balances) {
        this.balances = balances;
    }
}
