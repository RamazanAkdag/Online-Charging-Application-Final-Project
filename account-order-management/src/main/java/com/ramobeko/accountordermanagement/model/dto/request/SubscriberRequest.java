package com.ramobeko.accountordermanagement.model.dto.request;

import com.ramobeko.accountordermanagement.model.dto.IDTO;

import java.util.Date;

public class SubscriberRequest implements IDTO {

    private Long packageId;  // 📌 `customerId` kaldırıldı
    private Date startDate;
    private Date endDate;
    private String status;   // 📌 `phoneNumber` kaldırıldı

    // 🛠 Default Constructor
    public SubscriberRequest() {}

    // 🛠 Parameterized Constructor
    public SubscriberRequest(Long packageId, Date startDate, Date endDate, String status) {
        this.packageId = packageId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // ✅ Getters and Setters
    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
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
