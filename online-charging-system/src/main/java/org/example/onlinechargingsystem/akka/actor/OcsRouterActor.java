package org.example.onlinechargingsystem.akka.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;
import com.ramobeko.akka.Command;
import com.ramobeko.akka.CommonServiceKeys;

import java.util.List;

public class OcsRouterActor extends AbstractBehavior<Object> {

    private final List<ActorRef<Command.UsageData>> workerActors;
    private int counter = 0; // Round Robin dağıtım için

    public static Behavior<Object> create(List<ActorRef<Command.UsageData>> workers) {
        return Behaviors.setup(context -> {
            context.getSystem().receptionist().tell(
                    Receptionist.register(CommonServiceKeys.OCS_ROUTER_KEY, context.getSelf().narrow())
            );
            return new OcsRouterActor(context, workers);
        });
    }

    private OcsRouterActor(ActorContext<Object> context, List<ActorRef<Command.UsageData>> workers) {
        super(context);
        this.workerActors = workers;
    }

    @Override
    public Receive<Object> createReceive() {
        return newReceiveBuilder()
                .onMessage(Command.UsageData.class, this::onUsageData)
                .build();
    }

    private Behavior<Object> onUsageData(Command.UsageData data) {
        workerActors.get(counter % workerActors.size()).tell(data);
        counter++;
        return this;
    }
}
