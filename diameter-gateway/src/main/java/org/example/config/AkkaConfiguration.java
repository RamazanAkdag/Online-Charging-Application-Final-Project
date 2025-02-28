package org.example.config;


import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.Behaviors;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AkkaConfiguration {

    @Bean
    public ActorSystem<Void> actorSystem(){
        Config config = ConfigFactory.load("ocs.conf");
        ActorSystem<Void> system = akka.actor.typed.ActorSystem.create(Behaviors.empty(), "OCSSystem", config);
        return system;
    }



}
