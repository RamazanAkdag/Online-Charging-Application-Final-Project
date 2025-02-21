package com.ramobeko.accountordermanagement.model.entity.ignite;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import java.io.Serializable;
import java.util.Date;

public class IgniteBalance implements Serializable {

    @QuerySqlField(index = true)
    private Long id;

    @QuerySqlField(index = true)
    private Long subscriberId;

    @QuerySqlField(index = true)
    private Long packageId;

    @QuerySqlField
    private Long levelMinutes;

    @QuerySqlField
    private Long levelSms;

    @QuerySqlField
    private Long levelData;

    @QuerySqlField
    private Date startDate;

    @QuerySqlField
    private Date endDate;

    // Constructors
    public IgniteBalance() {
    }

    public IgniteBalance(Long id, Long subscriberId, Long packageId, Long levelMinutes, Long levelSms, Long levelData, Date startDate, Date endDate) {
        this.id = id;
        this.subscriberId = subscriberId;
        this.packageId = packageId;
        this.levelMinutes = levelMinutes;
        this.levelSms = levelSms;
        this.levelData = levelData;
        this.startDate = startDate != null ? startDate : new Date();
        this.endDate = endDate;
    }

    // Getters and Setters
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

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
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
