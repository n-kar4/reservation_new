package com.nk.reservation.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nk.reservation.dto.LoginResponseDTO;
import com.nk.reservation.dto.UserDTO;
import com.nk.reservation.entity.ApplicationUser;
import com.nk.reservation.services.IAuthenticationServices;

import ch.qos.logback.classic.Logger;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {

    Logger logger = (Logger) LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private IAuthenticationServices authenticationServices;

    @PostMapping("/auth/register")
    public ApplicationUser register(@RequestBody UserDTO user) {
        logger.info("register");
        return authenticationServices.registerUser(user.username(), user.password()); //UserDTO->user is a record 
    }

    @PostMapping("/auth/login")
    public LoginResponseDTO login(@RequestBody UserDTO user) {
        logger.info("login");
        return authenticationServices.login(user.username(), user.password());
    }

    @RequestMapping("/user")
    public String user() {
        logger.info("login");
        return "login user";
    }

    @RequestMapping("/admin")
    public String admin() {
        logger.info("loginAdmin");
        return "login admin";
    }

}
