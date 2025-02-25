package org.example.app;

import org.example.http.abstrct.TrafficSender;
import org.example.service.UsageDataGenerator;
import org.example.config.HazelcastClientManager;
import org.example.config.HttpClientManager;
import org.example.http.concrete.HttpTrafficSender;
import org.example.repository.concrete.HazelcastSubscriberRepository;
import org.example.repository.abstrct.SubscriberRepository;

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

        // Bağımlılıklar enjekte edilerek ApplicationInitializer başlatılıyor
        ApplicationInitializer app = new ApplicationInitializer(clientManager, subscriberRepository, trafficSender, usageDataGenerator);
        app.run();
    }
}
