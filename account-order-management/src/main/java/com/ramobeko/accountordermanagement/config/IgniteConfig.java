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
    public CacheConfiguration<String, OracleCustomer> oracleCustomerCache(DataSource oracleDataSource) {
        CacheJdbcPojoStoreFactory<String, OracleCustomer> storeFactory = new CacheJdbcPojoStoreFactory<>();

        storeFactory.setDataSourceFactory(() -> oracleDataSource);
        storeFactory.setDialect(new OracleDialect());

        CacheConfiguration<String, OracleCustomer> cacheCfg = new CacheConfiguration<>("OracleCustomerCache");
        cacheCfg.setReadThrough(true);
        cacheCfg.setWriteThrough(true);
        cacheCfg.setCacheStoreFactory(storeFactory);

        return cacheCfg;
    }

    @Bean
    public IgniteSpringBean igniteInstance() {
        IgniteSpringBean igniteSpringBean = new IgniteSpringBean();
        igniteSpringBean.setConfiguration(igniteConfiguration());
        return igniteSpringBean;
    }

    @Bean
    public IgniteConfiguration igniteConfiguration() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setPeerClassLoadingEnabled(true);
        cfg.setClientMode(true);

        TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(Arrays.asList("127.0.0.1:47500"));
        discoverySpi.setIpFinder(ipFinder);
        cfg.setDiscoverySpi(discoverySpi);

        return cfg;
    }
}

