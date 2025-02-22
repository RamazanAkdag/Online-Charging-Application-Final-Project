package com.ramobeko.accountordermanagement.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class HazelcastConfig {
    private static final Logger logger = LoggerFactory.getLogger(HazelcastConfig.class);

    @Bean
    public HazelcastInstance hazelcastClient() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("hazelcast-cluster");

        // Küme üyelerini IP olarak tanımla
        clientConfig.getNetworkConfig()
                .addAddress("18.158.110.143:5701", "18.158.110.143:5702")
                .setSmartRouting(true)  // Akıllı yönlendirme
                .setRedoOperation(true) // Hata durumunda tekrar deneme
                .setConnectionTimeout(5000); // Bağlantı zaman aşımı (5 saniye)

        try {
            logger.info("Hazelcast client başlatılıyor...");
            HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
            logger.info("Hazelcast client başarıyla bağlandı.");
            return client;
        } catch (Exception e) {
            logger.error("Hazelcast client başlatılırken hata oluştu!", e);
            throw new RuntimeException("Hazelcast client başlatılamadı.", e);
        }
    }
}
