package org.example;

import java.io.Serializable;

public class UsageData implements Serializable {
    private final String userId;
    private final String serviceType;
    private final int usageAmount;

    public UsageData(String userId, String serviceType, int usageAmount) {
        this.userId = userId;
        this.serviceType = serviceType;
        this.usageAmount = usageAmount;
    }

    public String getUserId() { return userId; }
    public String getServiceType() { return serviceType; }
    public int getUsageAmount() { return usageAmount; }
}

