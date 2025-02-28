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
        HazelcastClientManager clientManager = new HazelcastClientManager("hazelcast-cluster", "127.0.0.1:5701");
        SubscriberRepository subscriberRepository = new HazelcastSubscriberRepository(clientManager.getSubscriberMap());


        TrafficSender trafficSender = new HttpTrafficSender("http://localhost:5855/usage", new HttpClientManager());

        UsageDataGenerator usageDataGenerator = new UsageDataGenerator(subscriberRepository);


        ISubscriberService subscriberService = new SubscriberService(subscriberRepository);
        ITrafficGeneratorService trafficGeneratorService = new TrafficGeneratorService(subscriberRepository, usageDataGenerator, trafficSender);

        ApplicationInitializer app = new ApplicationInitializer(clientManager, subscriberService, trafficGeneratorService);
        app.run();
    }
}
