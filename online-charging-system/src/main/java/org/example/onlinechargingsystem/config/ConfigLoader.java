package org.example.onlinechargingsystem.config;


import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.stereotype.Component;

@Component
public class ConfigLoader {
    private final Config config;

    public ConfigLoader() {
        this.config = ConfigFactory.load(); // application.conf dosyasını yükler
    }

    public String getAkkaHost() {
        return config.getString("akka.remote.artery.canonical.hostname");
    }

    public int getAkkaPort() {
        return config.getInt("akka.remote.artery.canonical.port");
    }
}

