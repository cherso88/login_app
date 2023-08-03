package com.chersoft.loginapp.exception;

import javax.security.auth.login.LoginException;

public class RolAlreadyExistsException extends LoginException {
    public RolAlreadyExistsException(String mensaje) {
        super(mensaje);
    }
}