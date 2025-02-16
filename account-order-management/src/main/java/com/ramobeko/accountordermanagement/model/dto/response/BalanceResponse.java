package com.ramobeko.accountordermanagement.model.dto.response;

import com.ramobeko.accountordermanagement.model.dto.IDTO;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleBalance;
import com.ramobeko.accountordermanagement.model.entity.oracle.OraclePackage;

import java.util.Date;

public class BalanceResponse implements IDTO {

    private Long id;
    private Long subscriberId;
    private OraclePackage packagePlan;
    private Long levelMinutes;
    private Long levelSms;
    private Long levelData;
    private Date startDate;
    private Date endDate;

    // 🛠 Default Constructor
    public BalanceResponse() {}

    // 🛠 Entity to DTO Constructor
    public BalanceResponse(OracleBalance balance) {
        this.id = balance.getId();
        this.subscriberId = balance.getSubscriber().getId();
        this.packagePlan = balance.getPackagePlan();
        this.levelMinutes = balance.getLevelMinutes();
        this.levelSms = balance.getLevelSms();
        this.levelData = balance.getLevelData();
        this.startDate = balance.getStartDate();
        this.endDate = balance.getEndDate();
    }

    // ✅ Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(Long subscriberId) {
        this.subscriberId = subscriberId;
    }

    public OraclePackage getPackagePlan() {
        return packagePlan;
    }

    public void setPackagePlan(OraclePackage packagePlan) {
        this.packagePlan = packagePlan;
    }

    public Long getLevelMinutes() {
        return levelMinutes;
    }

    public void setLevelMinutes(Long levelMinutes) {
        this.levelMinutes = levelMinutes;
    }

    public Long getLevelSms() {
        return levelSms;
    }

    public void setLevelSms(Long levelSms) {
        this.levelSms = levelSms;
    }

    public Long getLevelData() {
        return levelData;
    }

    public void setLevelData(Long levelData) {
        this.levelData = levelData;
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
}
