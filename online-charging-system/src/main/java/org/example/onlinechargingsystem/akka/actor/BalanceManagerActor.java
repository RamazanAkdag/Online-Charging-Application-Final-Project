package org.example.onlinechargingsystem.akka.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import com.ramobeko.akka.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.onlinechargingsystem.repository.ignite.IgniteSubscriberRepository;
import org.example.onlinechargingsystem.service.abstrct.IBalanceService;
import com.ramobeko.ignite.IgniteSubscriber;

public class BalanceManagerActor extends AbstractBehavior<Command.UsageData> {

    private static final Logger logger = LogManager.getLogger(BalanceManagerActor.class);
    private final IBalanceService balanceService;
    private final IgniteSubscriberRepository igniteSubscriberRepository;

    public static Behavior<Command.UsageData> create(IBalanceService balanceService, IgniteSubscriberRepository igniteSubscriberRepository) {
        return Behaviors.setup(context -> new BalanceManagerActor(context, balanceService, igniteSubscriberRepository));
    }

    private BalanceManagerActor(ActorContext<Command.UsageData> context, IBalanceService balanceService, IgniteSubscriberRepository igniteSubscriberRepository) {
        super(context);
        this.balanceService = balanceService;
        this.igniteSubscriberRepository = igniteSubscriberRepository;
    }

    @Override
    public Receive<Command.UsageData> createReceive() {
        return newReceiveBuilder()
                .onMessage(Command.UsageData.class, this::processBalanceDeduction)
                .build();
    }

    private Behavior<Command.UsageData> processBalanceDeduction(Command.UsageData data) {
        String actorId = getContext().getSelf().path().name();
        try {
            Long subscNumber = Long.parseLong(data.getSenderSubscNumber());

            balanceService.deductBalance(subscNumber, (int) data.getUsageAmount(), data.getUsageType());

            logger.info("✅ [{}] Successfully deducted balance for subscriber {} with usageType {}", actorId, subscNumber, data.getUsageType());

        } catch (Exception e) {
            logger.error("❌ [{}] Failed to process balance deduction: {}", actorId, e.getMessage(), e);
        }
        return this;
    }
}
