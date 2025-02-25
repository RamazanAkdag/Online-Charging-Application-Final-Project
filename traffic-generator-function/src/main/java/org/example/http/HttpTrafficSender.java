package org.example.http;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramobeko.akka.Command;

public class HttpTrafficSender implements TrafficSender {
    private final String endpoint;
    private final ObjectMapper objectMapper;
    private final HttpClientManager httpClientManager;

    public HttpTrafficSender(String endpoint, HttpClientManager httpClientManager) {
        this.endpoint = endpoint;
        this.objectMapper = new ObjectMapper();
        this.httpClientManager = httpClientManager;
    }

    @Override
    public void sendUsageData(Command.UsageData data) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(data);
            int responseCode = httpClientManager.sendPostRequest(endpoint, jsonPayload);
            System.out.println("HTTP Response Code: " + responseCode);
        } catch (Exception e) {
            System.err.println("Trafik verisi gönderilirken hata oluştu: " + e.getMessage());
        }
    }
}

