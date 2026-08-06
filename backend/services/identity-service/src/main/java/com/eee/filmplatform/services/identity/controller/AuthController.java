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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Authentication", description = "Authentication and authorization APIs")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

        private final AuthService authService;

        private final EmailVerificationService emailVerificationService;

        @Operation(summary = "Register a new user", description = "Create a new account with an email, username, and password.")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User registered successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid registration payload", content = @Content)
        })
        @PostMapping("/register")
        @ResponseStatus(HttpStatus.CREATED)
        public ApiResponse<UserResponse> register(
                        @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Registration details for a new user", required = true, content = @Content(schema = @Schema(implementation = RegisterRequest.class))) RegisterRequest request) {

                return ApiResponse.success(
                                "User registered successfully",
                                authService.register(request));
        }

        @Operation(summary = "Login", description = "Authenticate a user using username/email and password.")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid login payload", content = @Content)
        })
        @PostMapping("/login")
        public ApiResponse<LoginResponse> login(
                        @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Credentials used to authenticate the user", required = true, content = @Content(schema = @Schema(implementation = LoginRequest.class))) LoginRequest request) {

                return ApiResponse.success("Login successful",
                                authService.login(request));
        }

        @Operation(summary = "Refresh Access Token", description = "Generate a new access token using a valid refresh token.")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Access token refreshed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid refresh token payload", content = @Content)
        })
        @PostMapping("/refresh")
        public ApiResponse<RefreshTokenResponse> refresh(
                        @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Refresh token payload", required = true, content = @Content(schema = @Schema(implementation = RefreshTokenRequest.class))) RefreshTokenRequest request) {

                return ApiResponse.success(
                                "Access token refreshed successfully",
                                authService.refresh(request));
        }

        @Operation(summary = "Logout", description = "Invalidate the current refresh token and end the session.")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Logout successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid logout payload", content = @Content)
        })
        @PostMapping("/logout")
        public ApiResponse<Void> logout(
                        @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Refresh token used to log out", required = true, content = @Content(schema = @Schema(implementation = LogoutRequest.class))) LogoutRequest request) {

                authService.logout(request);

                return ApiResponse.success("Logout successful",
                                null);
        }

        @Operation(summary = "Verify Email", description = "Verify a user account using the email verification token.")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Email verified successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid or expired token", content = @Content)
        })
        @GetMapping("/verify")
        public ApiResponse<Void> verifyEmail(
                        @Parameter(description = "Verification token sent to the user", required = true) @RequestParam("token") String token) {

                String message = emailVerificationService.verify(token);

                return ApiResponse.success(message,
                                null);
        }

        @Operation(summary = "Request Password Reset", description = "Send a password reset email if the provided email exists.")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reset email request accepted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid email payload", content = @Content)
        })
        @PostMapping("/forgot-password")
        public ApiResponse<Void> forgotPassword(
                        @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Email address for the password reset request", required = true, content = @Content(schema = @Schema(implementation = ForgotPasswordRequest.class))) ForgotPasswordRequest request) {

                authService.forgotPassword(request);

                return ApiResponse.success(
                                "If the email exists, a password reset link has been sent.",
                                null);
        }

        @Operation(summary = "Reset Password", description = "Set a new password using the reset token.")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password reset successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid reset password payload", content = @Content)
        })
        @PostMapping("/reset-password")
        public ApiResponse<Void> resetPassword(
                        @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Reset password details including the token and new password", required = true, content = @Content(schema = @Schema(implementation = ResetPasswordRequest.class))) ResetPasswordRequest request) {

                authService.resetPassword(request);

                return ApiResponse.success(
                                "Password reset successfully.",
                                null);
        }
}