package org.example.config;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class HazelcastConfig {

    @Bean
    @Primary
    public HazelcastInstance primaryHazelcastInstance() {
        return Hazelcast.newHazelcastInstance();
    }

    // Diğer HazelcastInstance bean’lerini de tanımlayabilirsiniz
    // @Bean
    // public HazelcastInstance secondaryHazelcastInstance() {
    //     return Hazelcast.newHazelcastInstance();
    // }
}

