package com.ramobeko.akka;

import akka.serialization.jackson.CborSerializable;
import com.ramobeko.dgwtgf.model.UsageType;

import java.util.Date;

public interface Command extends CborSerializable {

    final class UsageData implements Command {
        private final UsageType usageType;
        private final double usageAmount;
        private final String senderSubscNumber;
        private final String receiverSubscNumber;
        private final Date usageTime;

        public UsageData(UsageType usageType, double usageAmount, String senderSubscNumber, String receiverSubscNumber, Date usageTime) {
            this.usageType = usageType;
            this.usageAmount = usageAmount;
            this.senderSubscNumber = senderSubscNumber;
            this.receiverSubscNumber = receiverSubscNumber;
            this.usageTime = usageTime; // Artık doğrudan Date olarak alınıyor
        }

        // Parametresiz constructor (Jackson için gerekli)
        public UsageData() {
            this.usageType = null;
            this.usageAmount = 0;
            this.senderSubscNumber = "";
            this.receiverSubscNumber = "";
            this.usageTime = new Date();
        }

        public UsageType getUsageType() { return usageType; }
        public double getUsageAmount() { return usageAmount; }
        public String getSenderSubscNumber() { return senderSubscNumber; }
        public String getReceiverSubscNumber() { return receiverSubscNumber; }
        public Date getUsageTime() { return usageTime; }

        @Override
        public String toString() {
            return "UsageData{" +
                    "usageType=" + usageType +
                    ", usageAmount=" + usageAmount +
                    ", senderSubscNumber='" + senderSubscNumber + '\'' +
                    ", receiverSubscNumber='" + receiverSubscNumber + '\'' +
                    ", usageTime=" + usageTime +
                    '}';
        }
    }

    interface Response extends CborSerializable {}

    final class Ack implements Response {
        public static final Ack INSTANCE = new Ack();
        private Ack() {}
    }
}
