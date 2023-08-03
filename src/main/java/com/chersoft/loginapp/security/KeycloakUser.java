package com.chersoft.loginapp.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakUser {
    private String username;
    private String email;
    private boolean enabled;
    private String firstName;
    private String lastName;
    private List<Credential> credentials;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Credential {
        private String type;
        private String value;
        private boolean temporary;

    }
}