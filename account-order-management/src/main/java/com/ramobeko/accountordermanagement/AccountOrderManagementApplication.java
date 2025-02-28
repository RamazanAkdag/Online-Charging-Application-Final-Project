package com.ramobeko.accountordermanagement;


import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableIgniteRepositories(basePackages = "com.ramobeko.accountordermanagement.repository.ignite")
@EnableJpaRepositories(basePackages = "com.ramobeko.accountordermanagement.repository.oracle")
public class AccountOrderManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountOrderManagementApplication.class, args);
    }

}
