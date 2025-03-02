package org.example.onlinechargingsystem;

import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableIgniteRepositories(basePackages = "org.example.onlinechargingsystem.repository.ignite")
public class OnlineChargingSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(OnlineChargingSystemApplication.class, args);
	}
}


