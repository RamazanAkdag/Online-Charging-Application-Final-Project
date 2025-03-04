package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DiameterGatewayApplication {

    private static final Logger logger = LogManager.getLogger(DiameterGatewayApplication.class);

    public static void main(String[] args) {
        logger.info("Starting DiameterGatewayApplication...");
        SpringApplication.run(DiameterGatewayApplication.class, args);
        logger.info("DiameterGatewayApplication started successfully.");
    }
}
