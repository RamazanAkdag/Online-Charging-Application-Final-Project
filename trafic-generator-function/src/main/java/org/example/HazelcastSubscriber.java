package org.example;

import java.io.Serializable;
import java.util.Date;

public class HazelcastSubscriber {

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

    public HazelcastSubscriber(Long id, String name, String email, String role, Date startDate, String address, String status) {
        this.id = id;
        this.customerId = id; // Assuming customerId is the same as id
        this.packagePlanId = null; // No package plan in constructor
        this.phoneNumber = email; // Placeholder assignment
        this.startDate = startDate;
        this.endDate = null; // Default null for end date
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

