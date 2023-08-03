package com.chersoft.loginapp.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class Properties {

    @Value("${keycloak.grant_type}")
    private String grantType;

    @Value("${keycloak.client_id}")
    private String clientId;

    @Value("${keycloak.url_token}")
    private String keycloakUrlToken;

    @Value("${keycloak.url_admin_token}")
    private String keycloakUrlAdminToken;



    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${keycloak.client_secret}")
    private String keycloakSecret;

    @Value("${keycloak.url_check_token}")
    private String keycloakUrlCheckToken;

    @Value("${keycloak.url_new_user}")
    private String keycloakNewUserUrl;

    @Value("${keycloak.grant_type_client_credentials}")
    private String grantTypeClientCredentials;

    @Value("${keycloak.client_secret_admin_cli}")
    private String keycloakSecretAdminCli;

    @Value(("${keycloak.url_new_rol}"))
    private String keycloakNewRolUrl;


}
