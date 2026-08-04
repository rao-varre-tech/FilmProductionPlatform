package com.eee.filmplatform.services.identity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/api/v1/users/me")
    public String me(Authentication authentication) {

        return "Hello " + authentication.getName();

    }
}