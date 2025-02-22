package org.example;

import com.hazelcast.map.IMap;

import java.io.Serializable;
import java.util.Map;

// Aboneleri yöneten sınıf

public class SubscriberService {
    private final IMap<String, Long> subscriberMap;

    public SubscriberService(IMap<String, Long> subscriberMap) {
        this.subscriberMap = subscriberMap;
    }

    public void printSubscribers() {
        System.out.println("----- Güncel Aboneler -----");
        if (subscriberMap.isEmpty()) {
            System.out.println("Hazelcast'te kayıtlı abone bulunamadı.");
        } else {
            for (Map.Entry<String, Long> entry : subscriberMap.entrySet()) {
                System.out.println("Telefon: " + entry.getKey() + " -> ID: " + entry.getValue());
            }
        }
    }

}
