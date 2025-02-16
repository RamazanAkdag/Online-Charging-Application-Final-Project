package com.ramobeko.accountordermanagement.model.dto.request;


import com.ramobeko.accountordermanagement.model.dto.IDTO;

import java.util.Date;

public class SubscriberRequest implements IDTO {

    private Long customerId;
    private Long packageId;
    private String phoneNumber;
    private Date startDate;
    private Date endDate;
    private String status;

    // 🛠 Default Constructor
    public SubscriberRequest() {}

    // 🛠 Parameterized Constructor
    public SubscriberRequest(Long customerId, Long packageId, String phoneNumber, Date startDate, Date endDate, String status) {
        this.customerId = customerId;
        this.packageId = packageId;
        this.phoneNumber = phoneNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // ✅ Getters and Setters
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
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
}
