package org.example.onlinechargingsystem.config;

import com.ramobeko.akka.Command;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.ActorRef;
import akka.actor.typed.javadsl.Behaviors;
import org.example.onlinechargingsystem.akka.actor.OcsRouterActor;
import org.example.onlinechargingsystem.akka.actor.OcsWorkerActor;
import org.example.onlinechargingsystem.service.abstrct.IKafkaProducerService;
import org.apache.ignite.Ignite;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AkkaConfig {

    @Bean
    public ActorSystem<Command.UsageData> actorSystem(Ignite ignite, IKafkaProducerService kafkaProducerService) {
        List<ActorRef<Command.UsageData>> workerActors = new ArrayList<>();

        // Worker Actor'leri oluşturup ActorRef olarak listeye ekleyelim
        ActorSystem<Void> rootSystem = ActorSystem.create(Behaviors.setup(context -> {
            for (int i = 0; i < 4; i++) {
                ActorRef<Command.UsageData> worker = context.spawn(OcsWorkerActor.create(ignite, kafkaProducerService), "OcsWorker-" + i);
                workerActors.add(worker);
            }
            return Behaviors.empty();
        }), "OcsRoot");

        // Router Actor'ü başlat ve Worker'ları ona bağla
        return ActorSystem.create(OcsRouterActor.create(workerActors), "OcsRouter");
    }
}
