package org.example.http.concrete;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramobeko.dgwtgf.model.UsageRequest;
import org.example.http.abstrct.TrafficSender;
import org.example.config.HttpClientManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpTrafficSender implements TrafficSender {

    private static final Logger logger = LogManager.getLogger(HttpTrafficSender.class);
    private final String endpoint;
    private final ObjectMapper objectMapper;
    private final HttpClientManager httpClientManager;

    public HttpTrafficSender(String endpoint, HttpClientManager httpClientManager) {
        this.endpoint = endpoint;
        this.objectMapper = new ObjectMapper();
        this.httpClientManager = httpClientManager;
    }

    @Override
    public void sendUsageData(UsageRequest data) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(data);
            logger.debug("Sending usage data to endpoint: {}, payload: {}", endpoint, jsonPayload);

            int responseCode = httpClientManager.sendPostRequest(endpoint, jsonPayload);
            logger.info("HTTP Response Code: {}", responseCode);

            if (responseCode >= 400) {
                logger.warn("Received non-successful response code: {}", responseCode);
            }

        } catch (Exception e) {
            logger.error("Error sending usage data to endpoint: {}", endpoint, e);
        }
    }
}