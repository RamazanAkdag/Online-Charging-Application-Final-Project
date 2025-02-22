package org.example;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.ramobeko.akka.Command;

public class DgwActor extends AbstractBehavior<Command> {
    private final HazelcastInstance hazelcastInstance;

    private DgwActor(ActorContext<Command> context, HazelcastInstance hazelcastInstance) {
        super(context);
        this.hazelcastInstance = hazelcastInstance;
    }

    public static Behavior<Command> create(HazelcastInstance hazelcastInstance) {
        return Behaviors.setup(context -> new DgwActor(context, hazelcastInstance));
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Command.UsageData.class, this::processUsageData)
                .build();
    }

    private Behavior<Command> processUsageData(Command.UsageData data) {
        IMap<String, Long> userCache = hazelcastInstance.getMap("subscriberCache");

        String actorId = getContext().getSelf().path().name(); // Actor'ün adını al

        System.out.println("🔥 [" + actorId + "] Baba merhaba!");

        if (userCache.containsKey(data.getUserId())) {
            getContext().getLog().info("✅ [{}] Kullanıcı doğrulandı: {}", actorId, data.getUserId());
            getContext().getLog().info("🔹 [{}] Kullanım Türü: {}", actorId, data.getServiceType());
            getContext().getLog().info("🔹 [{}] Kullanım Miktarı: {}", actorId, data.getUsageAmount());
        } else {
            getContext().getLog().info("❌ [{}] Kullanıcı bulunamadı: {}", actorId, data.getUserId());
        }

        return this;
    }

}