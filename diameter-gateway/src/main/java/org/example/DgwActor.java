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
        IMap<String, Boolean> userCache = hazelcastInstance.getMap("userCache");

        if (userCache.containsKey(data.getUserId())) {
            getContext().getLog().info("✅ Kullanıcı doğrulandı: {}", data.getUserId());
            getContext().getLog().info("🔹 Kullanım Türü: {}", data.getServiceType());
            getContext().getLog().info("🔹 Kullanım Miktarı: {}", data.getUsageAmount());
            data.getReplyTo().tell(Command.Ack.INSTANCE);
        } else {
            getContext().getLog().info("❌ Kullanıcı bulunamadı: {}", data.getUserId());
        }

        return this;
    }
}