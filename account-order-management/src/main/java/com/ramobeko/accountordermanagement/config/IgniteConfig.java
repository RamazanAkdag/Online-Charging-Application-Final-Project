package com.ramobeko.accountordermanagement.config;

import org.apache.ignite.Ignition;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class IgniteConfig {

    @Value("${ignite.discovery.addresses}")
    private String igniteAddresses;

    /*@Bean
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
*/
    @Bean
    public IgniteClient igniteInstance() {
        return Ignition.startClient(new ClientConfiguration().setAddresses(igniteAddresses));
    }
}