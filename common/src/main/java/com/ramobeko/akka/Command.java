package com.ramobeko.akka;

import akka.actor.typed.ActorRef;
import akka.serialization.jackson.CborSerializable;

public interface Command extends CborSerializable {

    final class UsageData implements Command {
        private final String userId;
        private final String serviceType;
        private final int usageAmount;
        private final ActorRef<Response> replyTo;

        public UsageData(String userId, String serviceType, int usageAmount, ActorRef<Response> replyTo) {
            this.userId = userId;
            this.serviceType = serviceType;
            this.usageAmount = usageAmount;
            this.replyTo = replyTo;
        }

        public String getUserId() { return userId; }
        public String getServiceType() { return serviceType; }
        public int getUsageAmount() { return usageAmount; }
        public ActorRef<Response> getReplyTo() { return replyTo; }
    }

    interface Response extends CborSerializable {}

    final class Ack implements Response {
        public static final Ack INSTANCE = new Ack();
        private Ack() {}
    }
}