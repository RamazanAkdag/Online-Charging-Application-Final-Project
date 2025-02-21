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
        clientConfig.setClusterName("hazelcast-cluster"); // Cluster adını config ile aynı yap
        clientConfig.getNetworkConfig().addAddress("18.185.136.179:5701", "18.185.136.179:5702");

        // Hazelcast istemcisine bağlan
        HazelcastInstance hazelcastClient = HazelcastClient.newHazelcastClient(clientConfig);

        // subscribers adlı bir map içindeki verileri çek
        IMap<String, Long> subscriberMap = hazelcastClient.getMap("subscriberCache");

        System.out.println("=== Hazelcast'ten Aboneleri Çekme ===");

        if (subscriberMap.isEmpty()) {
            System.out.println("Hazelcast'te kayıtlı abone bulunamadı.");
        } else {
            for (Map.Entry<String, Long> entry : subscriberMap.entrySet()) {
                System.out.println("Telefon: " + entry.getKey() + " -> ID: " + entry.getValue());
            }
        }

        // Bağlantıyı kapat
        hazelcastClient.shutdown();
    }
}
