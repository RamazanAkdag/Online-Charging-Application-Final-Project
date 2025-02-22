package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import com.ramobeko.akka.Command;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public
class TrafficSender {
    private final String endpoint;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TrafficSender(String endpoint) {
        this.endpoint = endpoint;
    }

    public void sendUsageData(Command.UsageData data) {
        try {
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonPayload = objectMapper.writeValueAsString(data);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            System.out.println("HTTP Response Code: " + responseCode);

            connection.disconnect();
        } catch (JsonProcessingException e) {
            System.err.println("JSON işleme hatası: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Trafik verisi gönderilirken hata oluştu: " + e.getMessage());
        }
    }
}