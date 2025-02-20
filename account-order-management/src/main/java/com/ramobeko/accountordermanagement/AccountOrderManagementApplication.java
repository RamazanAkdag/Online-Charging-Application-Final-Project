package com.ramobeko.accountordermanagement;

import com.ramobeko.accountordermanagement.config.HazelcastConfig;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = HazelcastConfig.class)
})
@EnableIgniteRepositories(basePackages = "com.ramobeko.accountordermanagement.repository.ignite")
@EnableJpaRepositories(basePackages = "com.ramobeko.accountordermanagement.repository.oracle")
public class AccountOrderManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountOrderManagementApplication.class, args);
    }

}
