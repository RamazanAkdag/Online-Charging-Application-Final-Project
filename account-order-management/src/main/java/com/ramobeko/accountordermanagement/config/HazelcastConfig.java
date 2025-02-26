package com.ramobeko.accountordermanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    @Bean
    public HazelcastInstance hazelcastClient() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("hazelcast-cluster");

        // Ağ yapılandırmasını düzenle
        clientConfig.getNetworkConfig()
                .addAddress("18.158.110.143:5701", "18.158.110.144:5701") // Doğru IP'leri ekle
                .setSmartRouting(false) // Gereksiz bağlantı denemelerini önle
                .setConnectionTimeout(10000); // Zaman aşımını 10 saniyeye çek

        return HazelcastClient.newHazelcastClient(clientConfig);
    }

}


/*
@Configuration
public class HazelcastConfig {

    @Bean
    public HazelcastInstance hazelcastInstance() {
        Config config = new Config();
        config.setClusterName("hazelcast-cluster");
        config.getJetConfig().setEnabled(true);//ramo

        // Network yapılandırması
        NetworkConfig networkConfig = config.getNetworkConfig();
        JoinConfig joinConfig = networkConfig.getJoin();
        joinConfig.getMulticastConfig().setEnabled(false);
        joinConfig.getTcpIpConfig().setEnabled(true)
                .addMember("18.158.110.143:5701")
                .addMember("18.158.110.143:5702");

        return Hazelcast.newHazelcastInstance(config);
    }


}
 */

