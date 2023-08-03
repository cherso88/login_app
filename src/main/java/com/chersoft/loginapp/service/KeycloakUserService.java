package com.chersoft.loginapp.service;

import com.chersoft.loginapp.client.KeycloakUserClient;
import com.chersoft.loginapp.configuration.Properties;
import com.chersoft.loginapp.security.KeycloakToken;
import com.chersoft.loginapp.security.KeycloakTokenState;
import com.chersoft.loginapp.security.KeycloakUser;
import com.chersoft.loginapp.security.RequestData;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.io.IOException;

@Service
public class KeycloakUserService {

    KeycloakUserClient keycloakUserClient;

    Properties properties;

    public KeycloakUserService(KeycloakUserClient keycloakUserClient, Properties properties) {
        this.keycloakUserClient = keycloakUserClient;
        this.properties = properties;
    }

    public KeycloakToken getUserToken(RequestData requestData) throws IOException, InterruptedException, LoginException {

        String user = requestData.getUser();
        String password = requestData.getPassword();

        if (user == null || password == null || user.equals("") || password.equals("")) {
            throw new IllegalArgumentException("One or more parameters are incorrect");
        }

        return keycloakUserClient.getUserToken(properties.getKeycloakUrlToken(), user, password, properties.getClientId(), properties.getGrantType(), properties.getKeycloakSecret());
    }

    public KeycloakToken getAdminToken() throws IOException, InterruptedException, LoginException {

        return keycloakUserClient.getAdminToken(properties.getKeycloakUrlAdminToken(), "admin-cli", properties.getGrantTypeClientCredentials(), properties.getKeycloakSecretAdminCli());
    }

    /*
    Check Token
     */
    public KeycloakTokenState checkToken(String token) throws LoginException, IOException, InterruptedException {

        if (token == null || token == null) {
            throw new IllegalArgumentException("Token is null or empty");
        }

        return keycloakUserClient.checkToken(properties.getKeycloakUrlCheckToken(), token, properties.getClientId(), properties.getKeycloakSecret());

    }

    public String addNewUser(String token, KeycloakUser keycloakUser) throws LoginException, IOException, InterruptedException {
        // if ( "errorMessage": "User exists with same username"
        return keycloakUserClient.addNewUser(properties.getKeycloakNewUserUrl(), token, keycloakUser);


    }
}
