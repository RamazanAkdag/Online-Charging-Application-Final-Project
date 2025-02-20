import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.example.HazelcastSubscriber;

import java.util.Collection;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TrafficGenerator {

    private static final int TRAFFIC_INTERVAL_MS = 2000; // 2 saniyede bir trafik üret
    private static final Random RANDOM = new Random();
    private static final String HAZELCAST_MAP_NAME = "subscribers"; // Hazelcast map adı
    private static final String HAZELCAST_CLUSTER_NAME = "hazelcast-cluster"; // ✅ Cluster adı düzeltildi

    // Trafik türleri
    private static final String[] SERVICE_TYPES = {"CALL", "SMS", "DATA"};

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TrafficTask(), 0, TRAFFIC_INTERVAL_MS);
        System.out.println("✅ TGF Başlatıldı. Hazelcast'ten aboneler çekiliyor ve trafik üretiliyor...");
    }

    static class TrafficTask extends TimerTask {
        @Override
        public void run() {
            HazelcastInstance hazelcastInstance = null;
            try {
                // ✅ Hazelcast Client'ı cluster adıyla başlat
                ClientConfig clientConfig = new ClientConfig();
                clientConfig.setClusterName(HAZELCAST_CLUSTER_NAME);
                hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);

                IMap<Long, HazelcastSubscriber> subscriberMap = hazelcastInstance.getMap(HAZELCAST_MAP_NAME);
                Collection<HazelcastSubscriber> subscribers = subscriberMap.values();

                if (subscribers.isEmpty()) {
                    System.out.println("⚠️ Hazelcast'te abone bulunamadı.");
                    return;
                }

                // Rastgele bir abone seç
                HazelcastSubscriber subscriber = subscribers.stream()
                        .skip(RANDOM.nextInt(subscribers.size()))
                        .findFirst()
                        .orElse(null);

                if (subscriber != null) {
                    // ✅ Hazelcast'ten alınan veriyi ekrana yazdır
                    System.out.println("📞 Seçilen Abone:");
                    System.out.println("Telefon Numarası: " + subscriber.getPhoneNumber());
                    System.out.println("Abone ID: " + subscriber.getId());
                    System.out.println("Müşteri ID: " + subscriber.getCustomerId());
                    System.out.println("Paket Plan ID: " + subscriber.getPackagePlanId());
                    System.out.println("Durum: " + subscriber.getStatus());
                    System.out.println("---------------------------------");

                    // ✅ Trafik verisini oluştur ve ekrana yazdır
                    String trafficData = generateTrafficData(subscriber.getPhoneNumber());
                    System.out.println("📡 Oluşturulan Trafik Verisi: " + trafficData);

                    // ✅ DGW'ye gönderme işlemi şimdilik yorum satırı olarak bırakıldı
                    // sendTrafficData(trafficData);
                }
            } catch (Exception e) {
                System.err.println("❌ Trafik verisi oluşturulurken hata oluştu: " + e.getMessage());
            } finally {
                if (hazelcastInstance != null) {
                    hazelcastInstance.shutdown(); // ✅ Hazelcast bağlantısını kapat
                }
            }
        }
    }

    // ✅ Rastgele trafik verisi üretir
    private static String generateTrafficData(String phoneNumber) {
        String serviceType = SERVICE_TYPES[RANDOM.nextInt(SERVICE_TYPES.length)];
        int usageAmount = RANDOM.nextInt(500) + 1; // 1 ile 500 arasında rastgele kullanım

        return String.format("{\"phoneNumber\": \"%s\", \"serviceType\": \"%s\", \"usageAmount\": %d}",
                phoneNumber, serviceType, usageAmount);
    }
}
