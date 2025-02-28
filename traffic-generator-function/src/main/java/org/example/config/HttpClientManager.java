package org.example.config;


import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientManager {

    private static final Logger logger = LogManager.getLogger(HttpClientManager.class);

    public int sendPostRequest(String endpoint, String jsonPayload) throws Exception {
        logger.info("Sending POST request to endpoint: {}", endpoint);
        logger.debug("Payload: {}", jsonPayload);

        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes("utf-8");
            os.write(input, 0, input.length);
        } catch (Exception e) {
            logger.error("Error writing payload to connection: {}", e.getMessage(), e);
            throw e;
        }

        int responseCode = connection.getResponseCode();
        logger.info("Received response code: {}", responseCode);

        connection.disconnect();

        return responseCode;
    }
}

