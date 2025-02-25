package org.example.service.concrete;

import com.ramobeko.akka.Command;
import org.example.http.abstrct.TrafficSender;
import org.example.repository.abstrct.SubscriberRepository;
import org.example.service.abstrct.ITrafficGeneratorService;
import org.example.util.UsageDataGenerator;

import java.util.Map;

public class TrafficGeneratorService implements ITrafficGeneratorService {
    private final SubscriberRepository subscriberRepository;
    private final TrafficSender trafficSender;
    private final UsageDataGenerator usageDataGenerator;

    public TrafficGeneratorService(SubscriberRepository subscriberRepository, UsageDataGenerator usageDataGenerator, TrafficSender trafficSender) {
        this.subscriberRepository = subscriberRepository;
        this.trafficSender = trafficSender;
        this.usageDataGenerator = usageDataGenerator;
    }

    @Override
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
