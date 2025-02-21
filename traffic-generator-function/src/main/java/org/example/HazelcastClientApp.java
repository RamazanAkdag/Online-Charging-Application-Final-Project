package org.example;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.util.Map;

public class HazelcastClientApp {
    public static void main(String[] args) {
        // Hazelcast istemci konfigürasyonu
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("hazelcast-cluster"); // Cluster adı, sunucu konfigürasyonu ile uyumlu olmalı
        clientConfig.getNetworkConfig().addAddress("127.0.0.1:5701", "127.0.0.1:5702");

        // Hazelcast istemcisine bağlan
        HazelcastInstance hazelcastClient = HazelcastClient.newHazelcastClient(clientConfig);

        // "subscriberCache" adlı map'ten verileri çek
        IMap<String, Long> subscriberMap = hazelcastClient.getMap("subscriberCache");

        System.out.println("=== Hazelcast'ten Aboneleri Çekme ===");

        // Sonsuz döngü: her 5 saniyede bir abone listesini kontrol et ve yazdır
        while (true) {
            System.out.println("----- Güncel Aboneler -----");
            if (subscriberMap.isEmpty()) {
                System.out.println("Hazelcast'te kayıtlı abone bulunamadı.");
            } else {
                for (Map.Entry<String, Long> entry : subscriberMap.entrySet()) {
                    System.out.println("Telefon: " + entry.getKey() + " -> ID: " + entry.getValue());
                }
            }
            try {
                // 5 saniye bekle
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.err.println("Döngü kesintiye uğradı: " + e.getMessage());
                break;
            }
        }

        // Döngüden çıkarsa istemci kapatılır
        hazelcastClient.shutdown();
    }
}
