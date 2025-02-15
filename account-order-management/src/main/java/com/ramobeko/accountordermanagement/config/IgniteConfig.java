package com.ramobeko.accountordermanagement.config;


import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import org.apache.ignite.Ignite;

import org.apache.ignite.IgniteSpringBean;
import org.apache.ignite.Ignition;


import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStoreFactory;
import org.apache.ignite.cache.store.jdbc.dialect.OracleDialect;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;

import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.cache.configuration.Factory;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;

@Configuration
public class IgniteConfig {
    @Bean
    public IgniteConfiguration igniteCfg() {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        igniteConfiguration.setClientMode(true)
                .setPeerClassLoadingEnabled(true);

        TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();

        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500"));

        discoverySpi.setIpFinder(ipFinder);
        igniteConfiguration.setDiscoverySpi(discoverySpi);


        return igniteConfiguration;
    }
}
