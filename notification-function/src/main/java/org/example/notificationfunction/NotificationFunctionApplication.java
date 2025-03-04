package org.example.notificationfunction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.ramobeko.oracle.model")
public class NotificationFunctionApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationFunctionApplication.class, args);
    }
}
