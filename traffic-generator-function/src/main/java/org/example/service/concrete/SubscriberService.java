package org.example.service.concrete;


import org.example.repository.abstrct.SubscriberRepository;
import org.example.service.abstrct.ISubscriberService;

import java.util.Map;

public class SubscriberService implements ISubscriberService {
    private final SubscriberRepository subscriberRepository;

    public SubscriberService(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    @Override
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

