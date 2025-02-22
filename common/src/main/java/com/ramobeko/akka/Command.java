package com.ramobeko.akka;

import akka.actor.typed.ActorRef;
import akka.serialization.jackson.CborSerializable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface Command extends CborSerializable {

    final class UsageData implements Command {
        private final String userId;
        private final String serviceType;
        private final int usageAmount;


        @JsonCreator
        public UsageData(
                @JsonProperty("userId") String userId,
                @JsonProperty("serviceType") String serviceType,
                @JsonProperty("usageAmount") int usageAmount
                 ) {
            this.userId = userId;
            this.serviceType = serviceType;
            this.usageAmount = usageAmount;

        }

        // Parametresiz constructor (Jackson için gerekli)
        public UsageData() {
            this.userId = "";
            this.serviceType = "";
            this.usageAmount = 0;

        }

        public String getUserId() { return userId; }
        public String getServiceType() { return serviceType; }
        public int getUsageAmount() { return usageAmount; }

    }

    interface Response extends CborSerializable {}

    final class Ack implements Response {
        public static final Ack INSTANCE = new Ack();
        private Ack() {}
    }
}
