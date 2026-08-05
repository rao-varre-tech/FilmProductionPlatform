package com.eee.filmplatform.services.identity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eee.filmplatform.services.identity.service.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    private final EmailService emailService;

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String user() {
        return "USER endpoint";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "ADMIN endpoint";
    }

    @GetMapping("/user-read")
    @PreAuthorize("hasAuthority('USER_READ')")
    public String userRead() {
        return "USER_READ permission";
    }

    @PostMapping("/test-email")
    @PreAuthorize("hasRole('USER')")
    public String send() {

        emailService.sendTextEmail(
                "xyz@gmail.com",
                "Film Platform",
                "SMTP works!");

        return "Email sent";
    }
}
