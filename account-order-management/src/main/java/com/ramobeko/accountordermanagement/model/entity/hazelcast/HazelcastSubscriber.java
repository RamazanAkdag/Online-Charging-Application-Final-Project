package com.ramobeko.accountordermanagement.model.entity.hazelcast;

import java.io.Serializable;
import java.util.Date;

public class HazelcastSubscriber implements Serializable {

    private Long id;
    private Long customerId;
    private Long packagePlanId;
    private String phoneNumber;
    private Date startDate;
    private Date endDate;
    private String status;

    // Default Constructor
    public HazelcastSubscriber() {}

    public HazelcastSubscriber(Long id, Long customerId, Long packagePlanId, String phoneNumber, Date startDate, Date endDate, String status) {
        this.id = id;
        this.customerId = customerId;
        this.packagePlanId = packagePlanId;
        this.phoneNumber = phoneNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getPackagePlanId() { return packagePlanId; }
    public void setPackagePlanId(Long packagePlanId) { this.packagePlanId = packagePlanId; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
