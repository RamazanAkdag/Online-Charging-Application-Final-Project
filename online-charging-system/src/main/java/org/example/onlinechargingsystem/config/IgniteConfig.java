package org.example.onlinechargingsystem.config;

import org.apache.ignite.*;
import org.apache.ignite.configuration.*;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Properties;

@Configuration
public class IgniteConfig {

    @Bean
    public Ignite igniteInstance() throws IgniteException {
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setClientMode(true);
        cfg.setPeerClassLoadingEnabled(true);

        // Discovery mekanizması
        TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500")); // Ignite node adresi
        discoverySpi.setIpFinder(ipFinder);
        cfg.setDiscoverySpi(discoverySpi);

        // Hibernate için Ignite Cache Konfigurasyonu
        CacheConfiguration<Long, Object> balanceCache = new CacheConfiguration<>("BalanceCache");
        balanceCache.setIndexedTypes(Long.class, org.example.onlinechargingsystem.model.entity.Balance.class);

        cfg.setCacheConfiguration(balanceCache);

        // Ignite başlat
        return Ignition.start(cfg);
    }

    @Bean
    public Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty(Environment.CACHE_REGION_FACTORY, "org.apache.ignite.cache.hibernate.IgniteRegionFactory");
        properties.setProperty(Environment.USE_SECOND_LEVEL_CACHE, "true");
        properties.setProperty(Environment.USE_QUERY_CACHE, "true");
        properties.setProperty("hibernate.ignite.cache.default", "BalanceCache"); // Ignite Cache kullan
        return properties;
    }
}
