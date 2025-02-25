package org.example.http;


import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientManager {
    public int sendPostRequest(String endpoint, String jsonPayload) throws Exception {
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        connection.disconnect();
        return responseCode;
    }
}

