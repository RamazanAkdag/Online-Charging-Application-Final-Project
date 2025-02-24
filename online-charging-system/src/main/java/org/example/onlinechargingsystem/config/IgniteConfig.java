package org.example.onlinechargingsystem.config;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.example.onlinechargingsystem.model.entity.Balance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import org.apache.ignite.configuration.CacheConfiguration;

@Configuration
public class IgniteConfig {

    @Value("${ignite.discovery.addresses}")
    private String igniteAddresses;

    @Bean
    public Ignite igniteInstance() throws IgniteException {
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setClientMode(true);
        cfg.setPeerClassLoadingEnabled(true);

        // Discovery mekanizmasını ayarla
        TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(Collections.singletonList(igniteAddresses));
        discoverySpi.setIpFinder(ipFinder);
        cfg.setDiscoverySpi(discoverySpi);

        // Balance Cache Configuration
        CacheConfiguration<Long, Balance> balanceCacheConfig = new CacheConfiguration<>("BalanceCache");
        balanceCacheConfig.setIndexedTypes(Long.class, Balance.class);
        cfg.setCacheConfiguration(balanceCacheConfig);

        return Ignition.start(cfg);
    }
}


