package com.chersoft.loginapp.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakTokenState {
    private String given_name;
    private String family_name;
    private String username;
    private String email;
    private boolean active;

    // TODO: VER DE AGREGAR LA LISTA DE ROLES
}
