package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DiameterGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiameterGatewayApplication.class, args);

        /*HazelcastInstance hazelcastInstance = HazelcastConfig.createHazelcastClient();

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

        ActorSystem<Void> system = ActorSystem.create(rootBehavior, "dgw-system");*/
    }
}
