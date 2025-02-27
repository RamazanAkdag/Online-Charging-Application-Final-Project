package org.example.onlinechargingsystem.akka.actor;

import akka.actor.typed.*;
import akka.actor.typed.javadsl.*;
import akka.actor.typed.receptionist.Receptionist;
import com.ramobeko.akka.Command;

import java.util.List;


import akka.actor.typed.Behavior;
import com.ramobeko.akka.ServiceKeys; // <-- Ortak ServiceKey burada

import java.util.List;

/**
 * OcsRouterActor, UsageData mesajlarını ilgili Worker aktörlerine Round Robin ile dağıtır.
 */
public class OcsRouterActor extends AbstractBehavior<Command.UsageData> {

    private final List<ActorRef<Command.UsageData>> workerActors;
    private int counter = 0; // Round Robin sayacı

    // Aktörün yaratılması
    public static Behavior<Command.UsageData> create(List<ActorRef<Command.UsageData>> workers) {
        return Behaviors.setup(context -> {
            // Receptionist'e kayıt: ServiceKeys.OCS_ROUTER_KEY
            context.getSystem().receptionist().tell(
                    Receptionist.register(ServiceKeys.OCS_ROUTER_KEY, context.getSelf())
            );
            return new OcsRouterActor(context, workers);
        });
    }

    private OcsRouterActor(ActorContext<Command.UsageData> context,
                           List<ActorRef<Command.UsageData>> workers) {
        super(context);
        this.workerActors = workers;
    }

    @Override
    public Receive<Command.UsageData> createReceive() {
        return newReceiveBuilder()
                .onMessage(Command.UsageData.class, this::routeMessage)
                .build();
    }

    /**
     * Gelen UsageData mesajını Round Robin yaklaşımıyla bir Worker aktöre yönlendirir.
     */
    private Behavior<Command.UsageData> routeMessage(Command.UsageData data) {
        workerActors.get(counter % workerActors.size()).tell(data);
        counter++;
        return this;
    }
}

