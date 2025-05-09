package org.example.onlinechargingsystem.config.ignite;


import org.apache.ignite.Ignition;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.BinaryConfiguration;
import org.apache.ignite.configuration.ClientConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@Configuration
public class IgniteConfig {

    @Value("${ignite.discovery.addresses}")
    private String igniteAddresses;

    @Bean
    public IgniteConfiguration igniteCfg() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setClientMode(true);

        TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(Collections.singletonList(igniteAddresses));
        discoverySpi.setIpFinder(ipFinder);
        cfg.setDiscoverySpi(discoverySpi);

        // **Binary Configuration ekleyerek sınıfları kaydet**
        BinaryConfiguration binaryCfg = new BinaryConfiguration();
        binaryCfg.setClassNames(List.of(
                "com.ramobeko.ignite.IgniteSubscriber",
                "com.ramobeko.ignite.IgniteBalance"
        ));
        cfg.setBinaryConfiguration(binaryCfg);

        return cfg;
    }

    @Bean
    public IgniteClient igniteInstance() {
        return Ignition.startClient(new ClientConfiguration().setAddresses(igniteAddresses));
    }
}
