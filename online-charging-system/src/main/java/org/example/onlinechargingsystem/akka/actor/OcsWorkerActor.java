package org.example.onlinechargingsystem.akka.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.ActorRef;
import akka.actor.typed.javadsl.*;

import com.ramobeko.akka.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.onlinechargingsystem.config.OcsWorkerConfig;

public class OcsWorkerActor extends AbstractBehavior<Command.UsageData> {

    private static final Logger logger = LogManager.getLogger(OcsWorkerActor.class);
    private final ActorRef<Command.UsageData> cgfPublisher;
    private final ActorRef<Command.UsageData> balanceManager;

    public static Behavior<Command.UsageData> create(OcsWorkerConfig config) {
        return Behaviors.setup(context -> new OcsWorkerActor(context, config));
    }

    private OcsWorkerActor(ActorContext<Command.UsageData> context, OcsWorkerConfig config) {
        super(context);


        this.cgfPublisher = context.spawn(
                CGFPublisherActor.create(config.getKafkaProducerService(), config.getCgfTopic()),
                "CgfPublisherActor"
        );

        this.balanceManager = context.spawn(
                BalanceManagerActor.create(config.getBalanceService(), config.getIgniteSubscriberRepository()),
                "BalanceManagerActor"
        );
    }

    @Override
    public Receive<Command.UsageData> createReceive() {
        return newReceiveBuilder()
                .onMessage(Command.UsageData.class, this::handleUsageData)
                .build();
    }

    private Behavior<Command.UsageData> handleUsageData(Command.UsageData data) {
        String actorId = getContext().getSelf().path().name();
        logger.info("📩 [{}] Received message - Forwarding to CgfPublisher & BalanceManager", actorId);

        cgfPublisher.tell(data);
        balanceManager.tell(data);

        return this;
    }
}
