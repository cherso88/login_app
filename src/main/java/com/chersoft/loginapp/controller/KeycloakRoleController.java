package com.chersoft.loginapp.controller;

import com.chersoft.loginapp.exception.UserAlreadyExistsException;
import com.chersoft.loginapp.security.KeycloakUser;
import com.chersoft.loginapp.security.RequestData;
import com.chersoft.loginapp.security.RequestRol;
import com.chersoft.loginapp.service.KeycloakRolService;
import com.chersoft.loginapp.service.KeycloakUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/v1/rol")
public class KeycloakRoleController {

    @Autowired
    KeycloakRolService keycloakRolService;
    @Autowired
    KeycloakUserService keycloakUserService;

    @ResponseBody
    @PostMapping("add")
    public ResponseEntity<?> addNewRole(@RequestBody RequestRol requestRol) {
        try {
            String tokenAdmin = keycloakUserService.getAdminToken().getAccess_token();
            return new ResponseEntity(keycloakRolService.addNewRol(tokenAdmin, requestRol.getName(), requestRol.getDescription()), HttpStatus.OK);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}


