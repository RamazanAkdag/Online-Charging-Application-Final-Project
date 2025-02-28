package com.ramobeko.dgwtgf.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class UsageRequest {

    private UsageType usageType;
    private double usageAmount;
    private String senderSubscNumber;
    private String receiverSubscNumber;
    private Date usageTime;

    public UsageRequest(UsageType usageType, double usageAmount, String senderSubscNumber, String receiverSubscNumber, LocalDateTime usageTime) {
        this.usageType = usageType;
        this.usageAmount = usageAmount;
        this.senderSubscNumber = senderSubscNumber;
        this.receiverSubscNumber = receiverSubscNumber;
        this.usageTime = Date.from(usageTime.toInstant(ZoneOffset.UTC)); // LocalDateTime'i Date'e dönüştür
    }

    public UsageRequest() {
    }

    public UsageType getUsageType() {
        return usageType;
    }

    public void setUsageType(UsageType usageType) {
        this.usageType = usageType;
    }

    public double getUsageAmount() {
        return usageAmount;
    }

    public void setUsageAmount(double usageAmount) {
        this.usageAmount = usageAmount;
    }

    public String getSenderSubscNumber() {
        return senderSubscNumber;
    }

    public void setSenderSubscNumber(String senderSubscNumber) {
        this.senderSubscNumber = senderSubscNumber;
    }

    public String getReceiverSubscNumber() {
        return receiverSubscNumber;
    }

    public void setReceiverSubscNumber(String receiverSubscNumber) {
        this.receiverSubscNumber = receiverSubscNumber;
    }

    public Date getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(Date usageTime) {
        this.usageTime = usageTime;
    }

    @Override
    public String toString() {
        return "UsageRequest{" +
                "usageType=" + usageType +
                ", usageAmount=" + usageAmount +
                ", senderSubscNumber='" + senderSubscNumber + '\'' +
                ", receiverSubscNumber='" + receiverSubscNumber + '\'' +
                ", usageTime=" + usageTime +
                '}';
    }
}