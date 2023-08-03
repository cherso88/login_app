package com.chersoft.loginapp.client;

import com.chersoft.loginapp.configuration.Properties;
import com.chersoft.loginapp.exception.UserAlreadyExistsException;
import com.chersoft.loginapp.security.KeycloakToken;
import com.chersoft.loginapp.security.KeycloakTokenState;
import com.chersoft.loginapp.security.KeycloakUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Component
public class KeycloakUserClient {

    Logger logger = LoggerFactory.getLogger(KeycloakUserClient.class);

    Properties properties;

    public KeycloakUserClient(Properties properties) {
        this.properties = properties;
    }

    public KeycloakToken getUserToken(String url, String user, String pass, String clientId, String grantType, String secret) throws IOException, InterruptedException, LoginException {
        HttpClient httpClient = HttpClient.newHttpClient();

        String formData = "grant_type=" + grantType + "&client_id=" + clientId + "&username=" + user + "&password=" + pass + "&client_secret=" + secret;

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formData.trim()))
                .build();

        return sendRequest(httpClient, httpRequest);
    }

    public KeycloakToken getAdminToken(String url, String clientId, String grantType, String secret) throws IOException, InterruptedException, LoginException {
        HttpClient httpClient = HttpClient.newHttpClient();

        String formData = "grant_type=" + grantType + "&client_id=" + clientId + "&client_secret=" + secret;

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formData.trim()))
                .build();

        return sendRequest(httpClient, httpRequest);
    }

    private KeycloakToken sendRequest(HttpClient httpClient, HttpRequest httpRequest) throws IOException, InterruptedException, LoginException {

        KeycloakToken keycloakToken = null;

        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            String responseBody = httpResponse.body();

            if (responseBody != null && httpResponse.statusCode() == 200) {
                keycloakToken = parseToKeycloakToken(responseBody);
            } else {
                throw new LoginException("Failed to login: Error Code: " + httpResponse.statusCode());
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return keycloakToken;
    }

    private KeycloakToken parseToKeycloakToken(String responseBody) {
        try {
            Gson gson = new Gson();
            KeycloakToken keycloakToken = gson.fromJson(responseBody, KeycloakToken.class);
            return keycloakToken;
        } catch (JsonSyntaxException e) {
            logger.error("Error to parse: " + e.getMessage());
            throw e;
        }
    }


    public KeycloakTokenState checkToken(String url, String token, String clientId, String secret) throws LoginException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        String formData = "token=" + token + "&client_id=" + clientId + "&username=" + "&client_secret=" + secret;

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formData.trim()))
                .build();

        return sendRequestCheckToken(httpClient, httpRequest);
    }

    private KeycloakTokenState sendRequestCheckToken(HttpClient httpClient, HttpRequest httpRequest) throws LoginException, IOException, InterruptedException {

        KeycloakTokenState keycloakTokenState = null;

        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            String responseBody = httpResponse.body();

            if (responseBody != null && httpResponse.statusCode() == 200) {
                keycloakTokenState = parseToKeycloakTokenState(responseBody);
            } else {
                throw new LoginException("Failed to login: Error Code: " + httpResponse.statusCode());
            }


        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return keycloakTokenState;
    }

    private KeycloakTokenState parseToKeycloakTokenState(String responseBody) {
        try {
            Gson gson = new Gson();
            KeycloakTokenState keycloakTokenState = gson.fromJson(responseBody, KeycloakTokenState.class);
            return keycloakTokenState;
        } catch (JsonSyntaxException e) {
            logger.error("Error to parse: " + e.getMessage());
            throw e;
        }
    }

    /*
    Add new user
     */
    public String addNewUser(String url, String token, KeycloakUser keycloakUser) throws LoginException, IOException, InterruptedException {

        HttpClient httpClient = HttpClient.newHttpClient();

        String jsonPayload = parseToString(keycloakUser);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
                .build();

        return sendRequestNewUser(httpClient, httpRequest);

    }

    private String sendRequestNewUser(HttpClient httpClient, HttpRequest httpRequest) throws IOException, InterruptedException, LoginException {

        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());


            if (httpResponse.statusCode() == 409 && httpResponse.body().contains("User exists with same username")) {
                logger.error("User exists with same username");
                throw new UserAlreadyExistsException("User exists with same username - Error Code:  " + httpResponse.statusCode());
            }

            if (httpResponse.body().isEmpty() && httpResponse.statusCode() == 201) {
                return "User added correctly";
            } else {
                throw new LoginException("Failed to login: Error Code: " + httpResponse.statusCode());
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private String parseToString(KeycloakUser keycloakUser) {

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(keycloakUser);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

        return jsonString;
    }
}
