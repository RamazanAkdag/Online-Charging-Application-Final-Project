package org.example.onlinechargingsystem.akka.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import com.ramobeko.akka.Command;
import com.ramobeko.dgwtgf.model.UsageType;
import com.ramobeko.kafka.message.NFKafkaMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.onlinechargingsystem.service.abstrct.IKafkaProducerService;
import org.example.onlinechargingsystem.service.abstrct.IBalanceService;
import java.time.Instant;

public class NFPublisherActor extends AbstractBehavior<Command.UsageData> {

    private static final Logger logger = LogManager.getLogger(NFPublisherActor.class);
    private final IKafkaProducerService kafkaProducerService;
    private final IBalanceService balanceService;
    private final String nfTopic;

    public static Behavior<Command.UsageData> create(IKafkaProducerService kafkaProducerService, IBalanceService balanceService, String nfTopic) {
        return Behaviors.setup(context -> new NFPublisherActor(context, kafkaProducerService, balanceService, nfTopic));
    }

    public NFPublisherActor(ActorContext<Command.UsageData> context, IKafkaProducerService kafkaProducerService, IBalanceService balanceService, String nfTopic) {
        super(context);
        this.kafkaProducerService = kafkaProducerService;
        this.balanceService = balanceService;
        this.nfTopic = nfTopic;
    }

    @Override
    public Receive<Command.UsageData> createReceive() {
        return newReceiveBuilder()
                .onMessage(Command.UsageData.class, this::handleThresholdUsages)
                .build();
    }

    private Behavior<Command.UsageData> handleThresholdUsages(Command.UsageData data) {
        String actorId = getContext().getSelf().path().name();
        long subscNumber = Long.parseLong(data.getSenderSubscNumber());
        double usageAmount = data.getUsageAmount();
        UsageType usageType = data.getUsageType();

        String notificationType = balanceService.evaluateUsageThreshold(subscNumber, usageAmount, usageType);

        if (notificationType != null) {
            logger.info("🔔 [{}] Threshold exceeded. Sending notification: {}", actorId, notificationType);
            sendNotification(subscNumber, notificationType);
        } else {
            logger.info("ℹ️ [{}] No threshold exceeded for subscriber {}", actorId, subscNumber);
        }

        return this;
    }


    private void sendNotification(long subscNumber, String notificationType) {
        try {
            // Create NF Kafka message
            NFKafkaMessage message = new NFKafkaMessage(notificationType, String.valueOf(subscNumber), Instant.now());

            // Send to Kafka using KafkaProducerService
            kafkaProducerService.sendNotificationData(this.nfTopic, message);

            logger.info("✅ [{}] Successfully sent NF message to Kafka: {}", getContext().getSelf().path().name(), message);
        } catch (Exception e) {
            logger.error("❌ Error sending NF Kafka message for subscriber {}: {}", subscNumber, e.getMessage());
        }
    }
}
