package org.example;

import com.hazelcast.map.IMap;

public class TrafficGeneratorFunctionApplication {
    public static void main(String[] args) {
        HazelcastClientManager clientManager = new HazelcastClientManager("hazelcast-cluster", "18.158.110.143:5701", "18.158.110.143:5702");
        SubscriberService subscriberService = new SubscriberService(clientManager.getSubscriberMap());
        TrafficSender trafficSender = new TrafficSender("http://localhost:9091/usage-data");
        TrafficGenerator trafficGenerator = new TrafficGenerator(clientManager.getSubscriberMap(), trafficSender);

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
