package org.example.onlinechargingsystem.config;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.receptionist.Receptionist;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.onlinechargingsystem.service.abstrct.IBalanceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ramobeko.akka.CommonServiceKeys;
import com.ramobeko.akka.Command;
import org.example.onlinechargingsystem.akka.actor.OcsWorkerActor;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AkkaConfig {

    private static final Logger logger = LogManager.getLogger(AkkaConfig.class);

    private final IBalanceService balanceService;

    public AkkaConfig(IBalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @Bean
    public ActorSystem<Void> actorSystem() {
        logger.info("🔄 Initializing Actor System...");
        ActorSystem<Void> system = ActorSystem.create(rootBehavior(balanceService), "ClusterSystem");
        logger.info("✅ Actor System successfully initialized.");
        return system;
    }

    private static Behavior<Void> rootBehavior(IBalanceService balanceService) {
        return Behaviors.setup(context -> {
            registerWorkers(context, balanceService);
            return Behaviors.empty();
        });
    }

    private static void registerWorkers(ActorContext<Void> context, IBalanceService balanceService) {
        Logger workerLogger = LogManager.getLogger("WorkerRegistration");
        List<ActorRef<Command.UsageData>> workers = new ArrayList<>();

        for (int i = 1; i <= 4; i++) {
            ActorRef<Command.UsageData> worker = context.spawn(OcsWorkerActor.create(balanceService), "OcsWorker-" + i);
            workers.add(worker);
            context.getSystem().receptionist().tell(Receptionist.register(CommonServiceKeys.OCS_SERVICE_KEY, worker));
            workerLogger.info("✅ OcsWorker-{} has been successfully registered.", i);
        }
    }
}
