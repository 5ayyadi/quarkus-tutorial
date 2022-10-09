package com.core.network;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class ApiClient {
    public static String getRequest(String targetURL) {
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(targetURL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer token")
                    .method("GET", HttpRequest.BodyPublishers.ofString("{\n\t\"phoneNumber\": \"09390084053\"\n}"))
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch(Exception e){
            return "Error";
        }
    }
}
