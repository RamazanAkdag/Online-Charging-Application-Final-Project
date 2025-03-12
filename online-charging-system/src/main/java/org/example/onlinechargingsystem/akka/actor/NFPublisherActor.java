package org.example.onlinechargingsystem.akka.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import com.ramobeko.akka.Command;
import com.ramobeko.dgwtgf.model.UsageType;
import com.ramobeko.ignite.IgniteBalance;
import com.ramobeko.kafka.message.NFKafkaMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.onlinechargingsystem.service.abstrct.IKafkaProducerService;
import org.example.onlinechargingsystem.service.abstrct.IBalanceService;
import org.example.onlinechargingsystem.strategy.abstrct.IUsageBalanceChecker;
import org.example.onlinechargingsystem.strategy.concrete.checker.UsageBalanceCheckerFactory;

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

        // Retrieve user balance
        IgniteBalance balance = balanceService.getBalance(subscNumber);

        // Get the correct balance based on usage type
        IUsageBalanceChecker balanceChecker = UsageBalanceCheckerFactory.getChecker(usageType);
        double initialBalance = balanceChecker.getAvailableBalance(balance);

        // Prevent division by zero
        if (initialBalance <= 0) {
            logger.warn("⚠️ [{}] Subscriber {} has zero or negative balance for usage type {}. Skipping threshold check.", actorId, subscNumber, usageType);
            return this;
        }

        double remainingBalance = initialBalance - usageAmount;
        double usagePercentage = ((initialBalance - remainingBalance) / initialBalance) * 100;

        logger.info("📢 [{}] Checking usage thresholds for subscriber: {} (UsageType: {}, UsagePercentage: {:.2f}%)",
                actorId, subscNumber, usageType, usagePercentage);

        // Send notifications based on usage percentage
        if (usagePercentage >= 100) {
            logger.error("🚨 [{}] User {} has COMPLETELY EXHAUSTED their balance for {}. Sending URGENT NF notification.", actorId, subscNumber, usageType);
            sendNotification(subscNumber, "100% Balance Used - Urgent Action Required!");
        } else if (usagePercentage >= 80) {
            logger.warn("🚨 [{}] User {} exceeded 80% balance for {}. Sending NF notification.", actorId, subscNumber, usageType);
            sendNotification(subscNumber, "80% Threshold Exceeded");
        } else if (usagePercentage >= 50) {
            logger.info("⚠️ [{}] User {} exceeded 50% balance for {}. Sending NF notification.", actorId, subscNumber, usageType);
            sendNotification(subscNumber, "50% Threshold Exceeded");
        }

        logger.info("✅ [{}] Forwarding usage data to CGFPublisher & BalanceManager", actorId);

        return this;
    }

    private void sendNotification(long subscNumber, String notificationType) {
        try {
            // Create NF Kafka message
            NFKafkaMessage message = new NFKafkaMessage(notificationType, String.valueOf(subscNumber), Instant.now().toString());

            // Send to Kafka using KafkaProducerService
            kafkaProducerService.sendNotificationData(this.nfTopic, message);

            logger.info("✅ [{}] Successfully sent NF message to Kafka: {}", getContext().getSelf().path().name(), message);
        } catch (Exception e) {
            logger.error("❌ Error sending NF Kafka message for subscriber {}: {}", subscNumber, e.getMessage());
        }
    }
}
