package org.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Routers;
import akka.cluster.typed.Cluster;
import akka.cluster.typed.Join;
import com.hazelcast.core.HazelcastInstance;
import com.ramobeko.akka.Command;

public class DiameterGatewayApplication {
    public static void main(String[] args) {

        HazelcastInstance hazelcastInstance = HazelcastConfig.createHazelcastClient();

        Behavior<Void> rootBehavior = Behaviors.setup(context -> {
            int poolSize = 4;

            ActorRef<Command> dgwRouter = context.spawn(
                    Routers.pool(poolSize,
                            Behaviors.supervise(DgwActor.create(hazelcastInstance))
                                    .onFailure(SupervisorStrategy.restart())
                    ),
                    "dgwRouter"
            );

            Cluster cluster = Cluster.get(context.getSystem());
            cluster.manager().tell(Join.create(cluster.selfMember().address()));

            new DgwHttpServer(context.getSystem(), dgwRouter).startHttpServer();

            return Behaviors.empty();
        });

        ActorSystem<Void> system = ActorSystem.create(rootBehavior, "dgw-system");
    }
}
