package org.example.onlinechargingsystem.config;

import org.apache.ignite.*;
import org.apache.ignite.configuration.*;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Properties;

@Configuration
public class IgniteConfig {

    @Value("${ignite.discovery.addresses}")
    private String igniteAddresses;

    @Bean
    public IgniteConfiguration igniteCfg() {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        igniteConfiguration.setClientMode(true)
                .setPeerClassLoadingEnabled(true);

        TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();

        ipFinder.setAddresses(Collections.singletonList(igniteAddresses));
        discoverySpi.setIpFinder(ipFinder);
        igniteConfiguration.setDiscoverySpi(discoverySpi);

        return igniteConfiguration;
    }
}
