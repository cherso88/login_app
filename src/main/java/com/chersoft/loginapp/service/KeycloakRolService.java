package com.chersoft.loginapp.service;

import com.chersoft.loginapp.client.KeycloakRolClient;
import com.chersoft.loginapp.client.KeycloakUserClient;
import com.chersoft.loginapp.configuration.Properties;
import com.chersoft.loginapp.security.*;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.io.IOException;

@Service
public class KeycloakRolService {

    KeycloakRolClient keycloakRolClient;

    Properties properties;

    public KeycloakRolService(KeycloakRolClient keycloakRolClient, Properties properties) {
        this.keycloakRolClient = keycloakRolClient;
        this.properties = properties;
    }

    public Object addNewRol(String adminToken, String rolName, String rolDescription) throws LoginException, IOException, InterruptedException {
        return keycloakRolClient.addNewRol(properties.getKeycloakNewRolUrl(), adminToken, rolName, rolDescription);
    }
}
