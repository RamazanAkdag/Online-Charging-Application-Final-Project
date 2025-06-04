package com.ramobeko.accountordermanagement.model.dto.request;

import com.ramobeko.accountordermanagement.model.dto.IDTO;
import java.util.Date;

public class SubscriberUpdateRequest implements IDTO {

    private Long subscriberId;  // Güncellenecek aboneliğin ID'si
    private Date startDate;
    private Date endDate;
    private String status;

    // 🛠 Default Constructor
    public SubscriberUpdateRequest() {}

    // 🛠 Parameterized Constructor
    public SubscriberUpdateRequest(Long subscriberId, Date startDate, Date endDate, String status) {
        this.subscriberId = subscriberId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // ✅ Getters and Setters
    public Long getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(Long subscriberId) {
        this.subscriberId = subscriberId;
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
