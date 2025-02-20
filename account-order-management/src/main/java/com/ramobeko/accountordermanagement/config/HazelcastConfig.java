package com.ramobeko.accountordermanagement.config;

import com.hazelcast.config.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    @Bean
    public Config hazelcastConfig() {
        Config config = new Config();
        config.setClusterName("hazelcast-cluster"); // Cluster adı

        // Ağ yapılandırması
        NetworkConfig networkConfig = config.getNetworkConfig();
        JoinConfig joinConfig = networkConfig.getJoin();

        // Multicast kapalı, çünkü Docker ortamında TCP/IP kullanıyoruz
        joinConfig.getMulticastConfig().setEnabled(false);

        // Hazelcast düğümlerine bağlan (Docker'daki container isimleri)
        joinConfig.getTcpIpConfig()
                .setEnabled(true)
                .addMember("hazelcast-node-1:5701")
                .addMember("hazelcast-node-2:5701");

        // Cache yapılandırması
        config.addMapConfig(
                new MapConfig()
                        .setName("customerCache")  // Cache adı
                        .setTimeToLiveSeconds(600) // 10 dakika sonra temizlensin
        );

        return config;
    }
}
