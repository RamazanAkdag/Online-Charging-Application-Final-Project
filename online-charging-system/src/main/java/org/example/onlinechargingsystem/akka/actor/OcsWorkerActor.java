package org.example.onlinechargingsystem.akka.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.ActorRef;
import akka.actor.typed.javadsl.*;

import com.ramobeko.akka.Command;
import com.ramobeko.dgwtgf.model.UsageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.onlinechargingsystem.config.akka.OcsWorkerConfig;
import org.example.onlinechargingsystem.service.abstrct.IBalanceService;

public class OcsWorkerActor extends AbstractBehavior<Command.UsageData> {

    private static final Logger logger = LogManager.getLogger(OcsWorkerActor.class);
    private final ActorRef<Command.UsageData> cgfPublisher;
    private final ActorRef<Command.UsageData> balanceManager;
    private final ActorRef<Command.UsageData> nfPublisher;
    private final IBalanceService balanceService;

    public static Behavior<Command.UsageData> create(OcsWorkerConfig config) {
        return Behaviors.setup(context -> new OcsWorkerActor(context, config));
    }

    private OcsWorkerActor(ActorContext<Command.UsageData> context, OcsWorkerConfig config) {
        super(context);
        this.balanceService = config.getBalanceService();

        this.cgfPublisher = context.spawn(
                CGFPublisherActor.create(config.getKafkaProducerService(), config.getCgfTopic()),
                "CgfPublisherActor"
        );

        this.balanceManager = context.spawn(
                BalanceManagerActor.create(
                        balanceService,
                        config.getIgniteSubscriberRepository(),
                        config.getKafkaProducerService(),
                        config.getAbmfTopic()
                ),
                "BalanceManagerActor"
        );

        this.nfPublisher = context.spawn(
                NFPublisherActor.create(config.getKafkaProducerService(), config.getBalanceService(), config.getNfTopic()),
                "NFPublisherActor"
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
        System.out.println("------------------------------------------------------------");
        logger.info("📩 [{}] Received message - Checking balance for usage type {}", actorId, data.getUsageType());

        long subscNumber = Long.parseLong(data.getSenderSubscNumber());
        double usageAmount = data.getUsageAmount();
        UsageType usageType = data.getUsageType();

        boolean sufficient = balanceService.hasSufficientBalance(subscNumber, usageAmount, usageType);

        if (!sufficient) {
            logger.warn("⛔ [{}] Insufficient balance for subscriber: {}", actorId, subscNumber);
            nfPublisher.tell(data);
            return this;
        }

        logger.info("✅ [{}] User has sufficient balance. Forwarding to CGFPublisher & BalanceManager", actorId);

        cgfPublisher.tell(data);
        balanceManager.tell(data);
        nfPublisher.tell(data);

        return this;
    }
}
