package com.ramobeko.accountordermanagement.config;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.Ignition;

import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
public class IgniteConfig {

    @Bean
    public IgniteConfiguration igniteCfg() {
        IgniteConfiguration cfg = new IgniteConfiguration();

        cfg.setClientMode(true);

        TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();


        ipFinder.setAddresses(Arrays.asList("localhost:47500"));
        discoverySpi.setIpFinder(ipFinder);
        cfg.setDiscoverySpi(discoverySpi);

        return cfg;
    }

    @Bean
    public Ignite igniteInstance() throws IgniteCheckedException {
        Ignite ignite = Ignition.start(igniteCfg());

        ignite.getOrCreateCache("CustomerCache");
        return ignite;
    }
}