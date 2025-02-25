package org.example.app;

import org.example.config.HazelcastClientManager;
import org.example.http.abstrct.TrafficSender;
import org.example.service.abstrct.ITrafficGeneratorService;
import org.example.service.abstrct.ISubscriberService;

public class ApplicationInitializer {
    private final ISubscriberService subscriberService;
    private final ITrafficGeneratorService trafficGeneratorService;
    private final HazelcastClientManager clientManager;

    public ApplicationInitializer(
            HazelcastClientManager clientManager,
            ISubscriberService subscriberService,
            ITrafficGeneratorService trafficGeneratorService) {

        this.clientManager = clientManager;
        this.subscriberService = subscriberService;
        this.trafficGeneratorService = trafficGeneratorService;
    }

    public void run() {
        System.out.println("=== Hazelcast'ten Aboneleri Çekme ve Trafik Üretme ===");
        try {
            while (true) {
                subscriberService.printSubscribers();
                trafficGeneratorService.generateAndSendUsageDataForAllSubscribers();
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            System.err.println("Döngü kesintiye uğradı: " + e.getMessage());
        } finally {
            clientManager.shutdown();
        }
    }
}
