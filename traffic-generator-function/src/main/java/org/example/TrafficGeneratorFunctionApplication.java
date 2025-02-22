package org.example;

import com.ramobeko.akka.Command;

// Ana sınıf
public class TrafficGeneratorFunctionApplication {
    public static void main(String[] args) {
        HazelcastClientManager clientManager = new HazelcastClientManager("hazelcast-cluster", "35.159.24.36:5701", "35.159.24.36:5702");
        SubscriberService subscriberService = new SubscriberService(clientManager.getSubscriberMap());
        TrafficGenerator trafficGenerator = new TrafficGenerator();
        TrafficSender trafficSender = new TrafficSender("http://localhost:9091/usage-data");

        System.out.println("=== Hazelcast'ten Aboneleri Çekme ve Trafik Üretme ===");
        try {
            while (true) {
                subscriberService.printSubscribers();
                Command.UsageData usageData = trafficGenerator.generateUsageData();
                trafficSender.sendUsageData(usageData);
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            System.err.println("Döngü kesintiye uğradı: " + e.getMessage());
        } finally {
            clientManager.shutdown();
        }
    }
}