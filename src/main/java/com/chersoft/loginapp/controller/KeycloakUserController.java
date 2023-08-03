package com.chersoft.loginapp.controller;

import com.chersoft.loginapp.client.KeycloakUserClient;
import com.chersoft.loginapp.exception.UserAlreadyExistsException;
import com.chersoft.loginapp.security.KeycloakUser;
import com.chersoft.loginapp.security.RequestData;
import com.chersoft.loginapp.service.KeycloakUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/v1/user")
public class KeycloakUserController {

    Logger logger = LoggerFactory.getLogger(KeycloakUserController.class);

    @Autowired
    KeycloakUserService keycloakUserService;

    @ResponseBody
    @GetMapping()
    public String getStatus() {
        try {
            logger.info("GET STATUS");
            return "Estado ok";
        } catch (Exception e) {
            return "Estado error";
        }
    }

    @ResponseBody
    @PostMapping("token")
    public ResponseEntity<?> getUserToken(@RequestBody RequestData requestData) {
        try {
            return new ResponseEntity(keycloakUserService.getUserToken(requestData), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @PostMapping("check")
    public ResponseEntity<?> checkToken(@RequestParam("token") String token ) {
        try {
            return new ResponseEntity(keycloakUserService.checkToken(token), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @PostMapping("add")
    public ResponseEntity<?> addNewUser(@RequestBody KeycloakUser keycloakUser ) {
        try {
            String tokenAdmin = keycloakUserService.getAdminToken().getAccess_token();
            return new ResponseEntity(keycloakUserService.addNewUser(tokenAdmin, keycloakUser), HttpStatus.OK);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
