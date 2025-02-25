package org.example.app;

import org.example.config.HazelcastClientManager;
import org.example.http.TrafficSender;
import org.example.service.TrafficGenerator;
import org.example.repository.SubscriberRepository;
import org.example.service.SubscriberService;
import org.example.service.UsageDataGenerator;

public class ApplicationInitializer {
    private final SubscriberService subscriberService;
    private final TrafficGenerator trafficGenerator;
    private final HazelcastClientManager clientManager;

    public ApplicationInitializer(
            HazelcastClientManager clientManager,
            SubscriberRepository subscriberRepository,
            TrafficSender trafficSender,
            UsageDataGenerator usageDataGenerator) {

        this.clientManager = clientManager;
        this.subscriberService = new SubscriberService(subscriberRepository);
        this.trafficGenerator = new TrafficGenerator(subscriberRepository, usageDataGenerator, trafficSender);
    }

    public void run() {
        System.out.println("=== Hazelcast'ten Aboneleri Çekme ve Trafik Üretme ===");
        try {
            while (true) {
                subscriberService.printSubscribers();
                trafficGenerator.generateAndSendUsageDataForAllSubscribers();
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            System.err.println("Döngü kesintiye uğradı: " + e.getMessage());
        } finally {
            clientManager.shutdown();
        }
    }
}
