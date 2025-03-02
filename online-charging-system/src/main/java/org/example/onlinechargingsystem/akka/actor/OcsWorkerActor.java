package org.example.onlinechargingsystem.akka.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import com.ramobeko.akka.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.onlinechargingsystem.service.abstrct.IBalanceService;
import com.ramobeko.dgwtgf.model.UsageType;

public class OcsWorkerActor extends AbstractBehavior<Command.UsageData> {

    private static final Logger logger = LogManager.getLogger(OcsWorkerActor.class);
    private final IBalanceService balanceService;

    public static Behavior<Command.UsageData> create(IBalanceService balanceService) {
        return Behaviors.setup(context -> new OcsWorkerActor(context, balanceService));
    }

    public OcsWorkerActor(ActorContext<Command.UsageData> context, IBalanceService balanceService) {
        super(context);
        this.balanceService = balanceService;
    }

    @Override
    public Receive<Command.UsageData> createReceive() {
        return newReceiveBuilder()
                .onMessage(Command.UsageData.class, this::processUsageData)
                .build();
    }

    private Behavior<Command.UsageData> processUsageData(Command.UsageData data) {
        String actorId = getContext().getSelf().path().name(); // Aktörün adını al

        // Log message with updated fields
        logger.info("📩 [{}] Message received - UsageType: {}, Amount: {}, Sender: {}, Receiver: {}, Time: {}",
                actorId, data.getUsageType(), data.getUsageAmount(),
                data.getSenderSubscNumber(), data.getReceiverSubscNumber(), data.getUsageTime());

        try {
            switch (data.getUsageType()) {
                case MINUTE:
                    balanceService.deductBalanceForMinutes(Long.parseLong(data.getSenderSubscNumber()), (int) data.getUsageAmount());
                    break;
                case SMS:
                    balanceService.deductBalanceForSms(Long.parseLong(data.getSenderSubscNumber()), (int) data.getUsageAmount());
                    break;
                case DATA:
                    balanceService.deductBalanceForData(Long.parseLong(data.getSenderSubscNumber()), (int) data.getUsageAmount());
                    break;
                default:
                    logger.warn("⚠️ [{}] Unknown usage type: {}", actorId, data.getUsageType());
            }
        } catch (Exception e) {
            logger.error("❌ [{}] Failed to process usage data: {}", actorId, e.getMessage(), e);
        }

        return this;
    }
}
