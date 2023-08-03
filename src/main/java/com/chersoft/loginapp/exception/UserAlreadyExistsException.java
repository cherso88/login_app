package com.chersoft.loginapp.exception;

import javax.security.auth.login.LoginException;

public class UserAlreadyExistsException extends LoginException {
    public UserAlreadyExistsException(String mensaje) {
        super(mensaje);
    }
}