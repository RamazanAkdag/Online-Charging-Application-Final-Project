package org.example;

import akka.actor.AbstractActor;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class DgwActor extends AbstractActor {
    private final HazelcastInstance hazelcastInstance;

    public DgwActor(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(UsageData.class, this::processUsageData)
                .build();
    }

    private void processUsageData(UsageData data) {
        IMap<String, Boolean> userCache = hazelcastInstance.getMap("userCache");

        if (userCache.containsKey(data.getUserId())) {
            System.out.println("✅ Kullanıcı doğrulandı: " + data.getUserId());
            System.out.println("🔹 Kullanım Türü: " + data.getServiceType());
            System.out.println("🔹 Kullanım Miktarı: " + data.getUsageAmount());
        } else {
            System.out.println("❌ Kullanıcı bulunamadı: " + data.getUserId());
        }
    }
}
