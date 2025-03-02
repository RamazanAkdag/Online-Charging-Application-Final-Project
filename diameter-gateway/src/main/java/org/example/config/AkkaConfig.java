package org.example.config;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Props;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.GroupRouter;
import akka.actor.typed.javadsl.Routers;
import com.ramobeko.akka.CommonServiceKeys;
import com.ramobeko.akka.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AkkaConfig {

    private static final Logger logger = LogManager.getLogger(AkkaConfig.class);

    @Bean
    public ActorSystem<Void> actorSystem() {
        logger.info("Starting Actor System...");
        ActorSystem<Void> system = ActorSystem.create(Behaviors.empty(), "ClusterSystem");

        logger.info("Akka Actor System successfully started.");
        return system;
    }

    @Bean
    public ActorRef<Command.UsageData> router(ActorSystem<Void> system) {
        logger.info("Creating GroupRouter for OcsWorker actors...");

        GroupRouter<Command.UsageData> groupRouter = Routers.group(CommonServiceKeys.OCS_SERVICE_KEY);
        ActorRef<Command.UsageData> router = system.systemActorOf(groupRouter, "ocsRouter", Props.empty());

        logger.info("GroupRouter successfully created.");
        return router;
    }
}
