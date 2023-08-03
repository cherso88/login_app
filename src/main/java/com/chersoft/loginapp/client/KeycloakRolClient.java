package com.chersoft.loginapp.client;

import com.chersoft.loginapp.configuration.Properties;
import com.chersoft.loginapp.exception.RolAlreadyExistsException;
import com.chersoft.loginapp.exception.UserAlreadyExistsException;
import com.chersoft.loginapp.security.KeycloakToken;
import com.chersoft.loginapp.security.KeycloakTokenState;
import com.chersoft.loginapp.security.KeycloakUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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
public class KeycloakRolClient {

    Logger logger = LoggerFactory.getLogger(KeycloakRolClient.class);

    Properties properties;

    public KeycloakRolClient(Properties properties) {
        this.properties = properties;
    }

    public String addNewRol(String url, String token, String rolName, String rolDescription) throws LoginException, IOException, InterruptedException {

        HttpClient httpClient = HttpClient.newHttpClient();

        String bodyInfo = createBodyInfo(rolName, rolDescription);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(bodyInfo, StandardCharsets.UTF_8))
                .build();

        return sendRequestNewUser(httpClient, httpRequest);

    }


    private String sendRequestNewUser(HttpClient httpClient, HttpRequest httpRequest) throws IOException, InterruptedException, LoginException {

        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 409 && httpResponse.body().contains("already exists")) {
                logger.error("Rol exists with same username");
                throw new RolAlreadyExistsException("Rol exists with same name - Error Code:  " + httpResponse.statusCode());
            }

            if (httpResponse.body().isEmpty() && httpResponse.statusCode() == 201) {
                return "Rol added correctly";
            } else {
                throw new LoginException("Failed to login: Error Code: " + httpResponse.statusCode());
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }


    private static String createBodyInfo(String rolName, String rolDescription) {
        String jsonPayload = "{"
                + "\"name\": \"" + rolName + "\","
                + "\"description\": \"" + rolDescription + "\""
                + "}";
        return jsonPayload;
    }

}
