package org.example.onlinechargingsystem.model.entity;

import java.io.Serializable;

public class UsageData implements Serializable {
    private String userId;
    private String serviceType;
    private long usageAmount;

    public UsageData() {
    }

    public UsageData(String userId, String serviceType, long usageAmount) {
        this.userId = userId;
        this.serviceType = serviceType;
        this.usageAmount = usageAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public long getUsageAmount() {
        return usageAmount;
    }

    public void setUsageAmount(long usageAmount) {
        this.usageAmount = usageAmount;
    }

    @Override
    public String toString() {
        return "UsageData{" +
                "userId='" + userId + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", usageAmount=" + usageAmount +
                '}';
    }
}
