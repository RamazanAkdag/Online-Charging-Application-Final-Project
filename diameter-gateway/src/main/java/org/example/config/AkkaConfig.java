package org.example.config;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.Behaviors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AkkaConfig {

    @Bean
    public ActorSystem<Object> actorSystem() {
        // Boş bir davranışla basit bir ActorSystem oluşturuyoruz.
        return ActorSystem.create(Behaviors.empty(), "ClusterSystem");
    }
}
