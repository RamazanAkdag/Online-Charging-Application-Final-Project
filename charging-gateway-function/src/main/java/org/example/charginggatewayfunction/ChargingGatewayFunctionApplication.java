package org.example.charginggatewayfunction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.ramobeko.oracle.model")
public class ChargingGatewayFunctionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChargingGatewayFunctionApplication.class, args);
    }

}
