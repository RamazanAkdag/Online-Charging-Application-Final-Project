package org.example.app;

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

import java.util.Arrays;
import java.util.List;

public class TrafficGeneratorFunctionApplication {
    public static void main(String[] args) {
        // Hazelcast bağlantısı
        HazelcastClientManager clientManager = new HazelcastClientManager("hazelcast-cluster", "18.158.110.143:5701", "18.158.110.143:5702");
        SubscriberRepository subscriberRepository = new HazelcastSubscriberRepository(clientManager.getSubscriberMap());

        // Trafik gönderimi için HTTP Client
        TrafficSender trafficSender = new HttpTrafficSender("http://localhost:9091/usage-data", new HttpClientManager());

        // Kullanım verisini dinamik olarak üreten sınıf
        List<String> usageTypes = Arrays.asList("SMS", "CALL", "DATA");
        UsageDataGenerator usageDataGenerator = new UsageDataGenerator(usageTypes);

        // Interface'ler kullanılarak bağımlılıklar enjekte ediliyor
        ISubscriberService subscriberService = new SubscriberService(subscriberRepository);
        ITrafficGeneratorService trafficGeneratorService = new TrafficGeneratorService(subscriberRepository, usageDataGenerator, trafficSender);

        // ApplicationInitializer başlatılıyor
        ApplicationInitializer app = new ApplicationInitializer(clientManager, subscriberService, trafficGeneratorService);
        app.run();
    }
}
