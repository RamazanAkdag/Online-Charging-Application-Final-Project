package org.example.onlinechargingsystem.akka.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import com.ramobeko.akka.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.onlinechargingsystem.repository.ignite.IgniteSubscriberRepository;
import org.example.onlinechargingsystem.service.abstrct.IBalanceService;
import org.example.onlinechargingsystem.service.abstrct.IKafkaProducerService;
import com.ramobeko.ignite.IgniteSubscriber;
import com.ramobeko.kafka.message.ABMFKafkaMessage;

public class BalanceManagerActor extends AbstractBehavior<Command.UsageData> {

    private static final Logger logger = LogManager.getLogger(BalanceManagerActor.class);
    private final IBalanceService balanceService;
    private final IgniteSubscriberRepository igniteSubscriberRepository;
    private final IKafkaProducerService kafkaProducerService;
    private final String abmfTopic;

    public static Behavior<Command.UsageData> create(IBalanceService balanceService,
                                                     IgniteSubscriberRepository igniteSubscriberRepository,
                                                     IKafkaProducerService kafkaProducerService,
                                                     String abmfTopic) {
        return Behaviors.setup(context -> new BalanceManagerActor(context, balanceService, igniteSubscriberRepository, kafkaProducerService, abmfTopic));
    }

    private BalanceManagerActor(ActorContext<Command.UsageData> context,
                                IBalanceService balanceService,
                                IgniteSubscriberRepository igniteSubscriberRepository,
                                IKafkaProducerService kafkaProducerService,
                                String abmfTopic) {
        super(context);
        this.balanceService = balanceService;
        this.igniteSubscriberRepository = igniteSubscriberRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.abmfTopic = abmfTopic;
    }

    @Override
    public Receive<Command.UsageData> createReceive() {
        return newReceiveBuilder()
                .onMessage(Command.UsageData.class, this::processBalanceDeduction)
                .build();
    }

    private Behavior<Command.UsageData> processBalanceDeduction(Command.UsageData data) {
        String actorId = getContext().getSelf().path().name();
        logger.info("🔹 [{}] Processing balance deduction for Subscriber: {}, UsageType: {}, Amount: {}",
                actorId, data.getSenderSubscNumber(), data.getUsageType(), data.getUsageAmount());

        try {
            Long subscNumber = Long.parseLong(data.getSenderSubscNumber());

            balanceService.deductBalance(subscNumber, (int) data.getUsageAmount(), data.getUsageType());

            logger.info("✅ [{}] Successfully deducted balance for subscriber {} with usageType {}", actorId, subscNumber, data.getUsageType());

            // ✅ Send Kafka Message
            ABMFKafkaMessage message = new ABMFKafkaMessage(subscNumber, data.getUsageType(), data.getUsageAmount());
            kafkaProducerService.sendABMFUsageData(abmfTopic, message);
            logger.info("📤 [{}] Kafka message sent to topic {}: {}", actorId, abmfTopic, message);

        } catch (Exception e) {
            logger.error("❌ [{}] Failed to process balance deduction: {}", actorId, e.getMessage(), e);
        }
        return this;
    }
}
