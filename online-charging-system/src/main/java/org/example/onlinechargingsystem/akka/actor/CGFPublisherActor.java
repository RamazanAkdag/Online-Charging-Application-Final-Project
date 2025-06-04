package org.example.onlinechargingsystem.akka.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ramobeko.akka.Command;
import org.example.onlinechargingsystem.service.abstrct.IKafkaProducerService;
import com.ramobeko.kafka.message.CGFKafkaMessage;
import org.example.onlinechargingsystem.util.mapper.CGFKafkaMessageMapper;

public class CGFPublisherActor extends AbstractBehavior<Command.UsageData> {

    private static final Logger logger = LogManager.getLogger(CGFPublisherActor.class);
    private final IKafkaProducerService kafkaProducerService;
    private final String cgfTopic;

    public static Behavior<Command.UsageData> create(IKafkaProducerService kafkaProducerService, String cgfTopic) {
        return Behaviors.setup(context -> new CGFPublisherActor(context, kafkaProducerService, cgfTopic));
    }

    private CGFPublisherActor(ActorContext<Command.UsageData> context, IKafkaProducerService kafkaProducerService, String cgfTopic) {
        super(context);
        this.kafkaProducerService = kafkaProducerService;
        this.cgfTopic = cgfTopic;
    }

    @Override
    public Receive<Command.UsageData> createReceive() {
        return newReceiveBuilder()
                .onMessage(Command.UsageData.class, this::publishToCgfTopic)
                .build();
    }

    private Behavior<Command.UsageData> publishToCgfTopic(Command.UsageData data) {
        String actorId = getContext().getSelf().path().name();
        logger.info("📢 [{}] Publishing to CGF Kafka topic: UsageType: {}, Amount: {}, Sender: {}, Receiver: {}, Time: {}",
                actorId, data.getUsageType(), data.getUsageAmount(),
                data.getSenderSubscNumber(), data.getReceiverSubscNumber(), data.getUsageTime());

        try {
            CGFKafkaMessage message = CGFKafkaMessageMapper.toKafkaMessage(data);

            kafkaProducerService.sendCGFUsageData(cgfTopic, message);
            logger.info("✅ [{}] Successfully sent CGF message to Kafka: {}", actorId, message);

        } catch (Exception e) {
            logger.error("❌ [{}] Failed to send CGF message to Kafka: {}", actorId, e.getMessage(), e);
        }

        return this;
    }
}
