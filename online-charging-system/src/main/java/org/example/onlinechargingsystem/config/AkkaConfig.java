package org.example.onlinechargingsystem.config;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.receptionist.Receptionist;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    @Bean
    public ActorSystem<Void> actorSystem() {
        logger.info("Initializing Actor System...");
        ActorSystem<Void> system = ActorSystem.create(rootBehavior(), "ClusterSystem");
        logger.info("Actor System successfully initialized.");
        return system;
    }

    private static Behavior<Void> rootBehavior() {
        return Behaviors.setup(context -> {
            registerWorkers(context);
            return Behaviors.empty();
        });
    }

    private static void registerWorkers(ActorContext<Void> context) {
        Logger workerLogger = LogManager.getLogger("WorkerRegistration");
        List<ActorRef<Command.UsageData>> workers = new ArrayList<>();

        for (int i = 1; i <= 4; i++) {
            ActorRef<Command.UsageData> worker = context.spawn(OcsWorkerActor.create(), "OcsWorker-" + i);
            workers.add(worker);
            context.getSystem().receptionist().tell(Receptionist.register(CommonServiceKeys.OCS_SERVICE_KEY, worker));
            workerLogger.info("OcsWorker-" + i + " has been successfully registered.");
        }
    }
}
