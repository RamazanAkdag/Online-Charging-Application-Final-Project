package org.example.app;

import org.example.config.ConfigLoader;
import org.example.http.abstrct.TrafficSender;
import org.example.util.UsageDataGenerator;
import org.example.config.HazelcastClientManager;
import org.example.config.HttpClientManager;
import org.example.http.concrete.HttpTrafficSender;
import org.example.repository.concrete.HazelcastSubscriberRepository;
import org.example.repository.abstrct.SubscriberRepository;
import org.example.service.abstrct.ITrafficGeneratorService;
import org.example.service.abstrct.ISubscriberService;
import org.example.service.concrete.TrafficGeneratorService;
import org.example.service.concrete.SubscriberService;

public class TrafficGeneratorFunctionApplication {
    public static void main(String[] args) {
        // config.properties dosyasını classpath üzerinden yükler
        ConfigLoader configLoader = new ConfigLoader();

        // Konfigürasyon değerlerini al
        String clusterName = configLoader.getProperty("hazelcast.cluster.name");
        String address = configLoader.getProperty("hazelcast.address");
        String trafficUrl = configLoader.getProperty("traffic.url");

        // Hazelcast ve HTTP client ayarlarını konfigürasyon değerlerinden al
        HazelcastClientManager clientManager = new HazelcastClientManager(clusterName, address);
        SubscriberRepository subscriberRepository = new HazelcastSubscriberRepository(clientManager.getSubscriberMap());

        TrafficSender trafficSender = new HttpTrafficSender(trafficUrl, new HttpClientManager());
        UsageDataGenerator usageDataGenerator = new UsageDataGenerator(subscriberRepository);

        ISubscriberService subscriberService = new SubscriberService(subscriberRepository);
        ITrafficGeneratorService trafficGeneratorService = new TrafficGeneratorService(
                subscriberRepository, usageDataGenerator, trafficSender
        );

        ApplicationInitializer app = new ApplicationInitializer(clientManager, subscriberService, trafficGeneratorService);
        app.run();
    }
}
