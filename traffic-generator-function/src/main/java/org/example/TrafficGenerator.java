package org.example;

import com.hazelcast.map.IMap;
import com.ramobeko.akka.Command;

import java.util.Map;
import java.util.Random;

public class TrafficGenerator {
    private static final String[] USAGE_TYPES = {"SMS", "CALL", "DATA"};
    private final Random random = new Random();
    private final IMap<String, Long> subscriberMap; // Hazelcast subscriber cache
    private final TrafficSender trafficSender;

    public TrafficGenerator(IMap<String, Long> subscriberMap, TrafficSender trafficSender) {
        this.subscriberMap = subscriberMap;
        this.trafficSender = trafficSender;
    }

    public void generateAndSendUsageDataForAllSubscribers() {
        if (subscriberMap.isEmpty()) {
            System.out.println("❌ Hazelcast'te kayıtlı abone yok, trafik üretilemiyor.");
            return;
        }

        System.out.println("🔄 Tüm aboneler için trafik verisi üretiliyor...");

        for (Map.Entry<String, Long> entry : subscriberMap.entrySet()) {
            String userId = entry.getKey();
            String usageType = USAGE_TYPES[random.nextInt(USAGE_TYPES.length)];
            int amount = random.nextInt(500);

            Command.UsageData usageData = new Command.UsageData(userId, usageType, amount);
            System.out.println("✔ Kullanım verisi üretildi -> UserID: " + userId + ", UsageType: " + usageType + ", Amount: " + amount);

            trafficSender.sendUsageData(usageData);
        }
    }
}
