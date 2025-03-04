package com.ramobeko.kafka.message;

import com.ramobeko.dgwtgf.model.UsageType;

import java.util.Date;

public class ABMFKafkaMessage {
    private Long senderSubscNumber;

    private UsageType usageType;
    private double usageAmount;


    // Parametreli Constructor
    public ABMFKafkaMessage(Long senderSubscNumber,  UsageType usageType, double usageAmount) {
        this.senderSubscNumber = senderSubscNumber;
        this.usageType = usageType;
        this.usageAmount = usageAmount;

    }
    public ABMFKafkaMessage() {
        // Jackson için boş constructor
    }


    // Getter ve Setter Metotları
    public Long getSenderSubscNumber() {
        return senderSubscNumber;
    }

    public void setSenderSubscNumber(Long senderSubscNumber) {
        this.senderSubscNumber = senderSubscNumber;
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



    @Override
    public String toString() {
        return "KafkaMessage{" +
                "senderSubscNumber='" + senderSubscNumber + '\'' +
                ", usageType=" + usageType +
                ", usageAmount=" + usageAmount +
                '}';
    }
}
