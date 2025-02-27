package org.example.onlinechargingsystem.controller;


import org.example.onlinechargingsystem.config.ConfigLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {

    private final ConfigLoader configLoader;

    public ConfigController(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    @GetMapping("/akka-config")
    public String getAkkaConfig() {
        return "Akka Host: " + configLoader.getAkkaHost() + ", Port: " + configLoader.getAkkaPort();
    }
}
