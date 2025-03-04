package org.example.onlinechargingsystem;

import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableIgniteRepositories(basePackages = "org.example.onlinechargingsystem.repository.ignite")
@EnableJpaRepositories(basePackages = "org.example.onlinechargingsystem.repository.jpa")
public class OnlineChargingSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(OnlineChargingSystemApplication.class, args);
	}
}


