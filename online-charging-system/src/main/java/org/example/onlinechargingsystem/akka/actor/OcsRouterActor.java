package org.example.onlinechargingsystem.akka.actor;

import akka.actor.typed.*;
import akka.actor.typed.javadsl.*;
import akka.actor.typed.receptionist.Receptionist;
import akka.actor.typed.receptionist.ServiceKey;
import com.ramobeko.akka.Command;

import java.util.List;

public class OcsRouterActor extends AbstractBehavior<Command.UsageData> {

    private final List<ActorRef<Command.UsageData>> workerActors;
    private int counter = 0; // Round Robin dağıtım için

    // OCS Router'ı Receptionist'a kaydetmek için ServiceKey tanımlıyoruz
    public static final ServiceKey<Command.UsageData> OCS_ROUTER_KEY =
            ServiceKey.create(Command.UsageData.class, "OcsRouter");

    public static Behavior<Command.UsageData> create(List<ActorRef<Command.UsageData>> workers) {
        return Behaviors.setup(context -> {
            context.getSystem().receptionist().tell(Receptionist.register(OCS_ROUTER_KEY, context.getSelf()));
            return new OcsRouterActor(context, workers);
        });
    }

    private OcsRouterActor(ActorContext<Command.UsageData> context, List<ActorRef<Command.UsageData>> workers) {
        super(context);
        this.workerActors = workers;
    }

    @Override
    public Receive<Command.UsageData> createReceive() {
        return newReceiveBuilder()
                .onMessage(Command.UsageData.class, this::routeMessage)
                .build();
    }

    private Behavior<Command.UsageData> routeMessage(Command.UsageData data) {
        workerActors.get(counter % workerActors.size()).tell(data); // Round Robin ile iş dağıtımı
        counter++;
        return this;
    }
}
