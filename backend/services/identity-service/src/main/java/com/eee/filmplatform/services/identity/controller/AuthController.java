package com.eee.filmplatform.services.identity.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eee.filmplatform.services.identity.common.response.ApiResponse;
import com.eee.filmplatform.services.identity.dto.request.ForgotPasswordRequest;
import com.eee.filmplatform.services.identity.dto.request.LoginRequest;
import com.eee.filmplatform.services.identity.dto.request.LogoutRequest;
import com.eee.filmplatform.services.identity.dto.request.RefreshTokenRequest;
import com.eee.filmplatform.services.identity.dto.request.RegisterRequest;
import com.eee.filmplatform.services.identity.dto.request.ResetPasswordRequest;
import com.eee.filmplatform.services.identity.dto.response.LoginResponse;
import com.eee.filmplatform.services.identity.dto.response.RefreshTokenResponse;
import com.eee.filmplatform.services.identity.dto.response.UserResponse;
import com.eee.filmplatform.services.identity.service.AuthService;
import com.eee.filmplatform.services.identity.service.EmailVerificationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

        private final AuthService authService;

        private final EmailVerificationService emailVerificationService;

        @PostMapping("/register")
        @ResponseStatus(HttpStatus.CREATED)
        public ApiResponse<UserResponse> register(
                        @Valid @RequestBody RegisterRequest request) {

                return ApiResponse.success("User registered successfully",
                                authService.register(request));
        }

        @PostMapping("/login")
        public ApiResponse<LoginResponse> login(
                        @Valid @RequestBody LoginRequest request) {

                return ApiResponse.success("Login successful",
                                authService.login(request));
        }

        @PostMapping("/refresh")
        public ApiResponse<RefreshTokenResponse> refresh(
                        @Valid @RequestBody RefreshTokenRequest request) {

                System.out.println("=== REFRESH ENDPOINT HIT ===");

                return ApiResponse.success("Access token refreshed successfully",
                                authService.refresh(request));
        }

        @PostMapping("/logout")
        public ApiResponse<Void> logout(
                        @Valid @RequestBody LogoutRequest request) {

                authService.logout(request);

                return ApiResponse.success("Logout successful",
                                null);
        }

        @GetMapping("/verify")
        public ApiResponse<Void> verifyEmail(
                        @RequestParam("token") String token) {

                String message = emailVerificationService.verify(token);

                return ApiResponse.success(message,
                                null);
        }

        @PostMapping("/forgot-password")
        public ApiResponse<Void> forgotPassword(
                        @Valid @RequestBody ForgotPasswordRequest request) {

                authService.forgotPassword(request);

                return ApiResponse.success("If the email exists, a password reset link has been sent.",
                                null);
        }

        @PostMapping("/reset-password")
        public ApiResponse<Void> resetPassword(
                        @Valid @RequestBody ResetPasswordRequest request) {

                authService.resetPassword(request);

                return ApiResponse.success("Password reset successfully.",
                                null);
        }
}