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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AkkaConfig {

    private static final Logger logger = LogManager.getLogger(AkkaConfig.class);

    @Value("${akka.actor.system.name}")
    private String actorSystemName;

    @Value("${akka.router.name}")
    private String routerName;

    @Bean
    public ActorSystem<Void> actorSystem() {
        logger.info("Starting Actor System with name: {}...", actorSystemName);
        ActorSystem<Void> system = ActorSystem.create(Behaviors.empty(), actorSystemName);
        logger.info("Akka Actor System successfully started with name: {}.", actorSystemName);
        return system;
    }

    @Bean
    public ActorRef<Command.UsageData> router(ActorSystem<Void> system) {
        logger.info("Creating GroupRouter for OcsWorker actors with router name: {}...", routerName);
        GroupRouter<Command.UsageData> groupRouter = Routers.group(CommonServiceKeys.OCS_SERVICE_KEY);
        ActorRef<Command.UsageData> router = system.systemActorOf(groupRouter, routerName, Props.empty());
        logger.info("GroupRouter successfully created with router name: {}.", routerName);
        return router;
    }
}
