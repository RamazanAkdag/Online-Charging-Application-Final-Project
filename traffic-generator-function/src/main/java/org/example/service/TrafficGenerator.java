package org.example;

import com.ramobeko.akka.Command;
import org.example.repository.SubscriberRepository;

import java.util.Map;

public class TrafficGenerator {
    private final SubscriberRepository subscriberRepository;
    private final TrafficSender trafficSender;
    private final UsageDataGenerator usageDataGenerator;

    public TrafficGenerator(SubscriberRepository subscriberRepository, UsageDataGenerator usageDataGenerator, TrafficSender trafficSender) {
        this.subscriberRepository = subscriberRepository;
        this.trafficSender = trafficSender;
        this.usageDataGenerator = usageDataGenerator;
    }

    public void generateAndSendUsageDataForAllSubscribers() {
        if (subscriberRepository.isEmpty()) {
            System.out.println("❌ Hazelcast'te kayıtlı abone yok, trafik üretilemiyor.");
            return;
        }

        System.out.println("🔄 Tüm aboneler için trafik verisi üretiliyor...");

        for (Map.Entry<String, Long> entry : subscriberRepository.getSubscribers().entrySet()) {
            String userId = entry.getKey();
            Command.UsageData usageData = usageDataGenerator.generateUsageData(userId);
            System.out.println("✔ Kullanım verisi üretildi -> " + usageData);

            trafficSender.sendUsageData(usageData);
        }
    }
}
