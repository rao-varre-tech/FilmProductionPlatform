package com.eee.filmplatform.services.identity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eee.filmplatform.services.identity.service.EmailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Testing", description = "Internal testing APIs")
@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    private final EmailService emailService;

    @Operation(summary = "User role test endpoint", description = "Endpoint available to users with the USER role.")
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String user() {
        return "USER endpoint";
    }

    @Operation(summary = "Admin role test endpoint", description = "Endpoint available to users with the ADMIN role.")
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "ADMIN endpoint";
    }

    @Operation(summary = "User read permission test endpoint", description = "Endpoint available to users with the USER_READ authority.")
    @GetMapping("/user-read")
    @PreAuthorize("hasAuthority('USER_READ')")
    public String userRead() {
        return "USER_READ permission";
    }

    @Operation(summary = "Send test email", description = "Send a test email to verify SMTP configuration.")
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
