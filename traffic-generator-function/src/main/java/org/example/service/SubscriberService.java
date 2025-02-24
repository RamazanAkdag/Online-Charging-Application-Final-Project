package org.example;

import java.util.Map;

public class SubscriberService {
    private final SubscriberRepository subscriberRepository;

    public SubscriberService(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    public void printSubscribers() {
        System.out.println("----- Güncel Aboneler -----");
        Map<String, Long> subscribers = subscriberRepository.getSubscribers();

        if (subscribers.isEmpty()) {
            System.out.println("Hazelcast'te kayıtlı abone bulunamadı.");
        } else {
            for (Map.Entry<String, Long> entry : subscribers.entrySet()) {
                System.out.println("Telefon: " + entry.getKey() + " -> ID: " + entry.getValue());
            }
        }
    }
}
