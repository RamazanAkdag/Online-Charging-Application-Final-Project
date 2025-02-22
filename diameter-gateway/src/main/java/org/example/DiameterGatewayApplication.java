package org.example;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import akka.cluster.typed.Cluster;
import akka.cluster.typed.Join;
import com.hazelcast.core.HazelcastInstance;



public class DiameterGatewayApplication {
    public static void main(String[] args) {



        HazelcastInstance hazelcastInstance = HazelcastConfig.createHazelcastClient();

        Behavior<Void> rootBehavior = Behaviors.setup(context -> {
            var dgwRouter = context.spawn(DgwActor.create(hazelcastInstance), "dgwRouter");

            Cluster cluster = Cluster.get(context.getSystem());
            cluster.manager().tell(Join.create(cluster.selfMember().address()));

            new DgwHttpServer(context.getSystem(), dgwRouter).startHttpServer();
            return Behaviors.empty();
        });

        ActorSystem<Void> system = ActorSystem.create(rootBehavior, "dgw-system");
    }
}
