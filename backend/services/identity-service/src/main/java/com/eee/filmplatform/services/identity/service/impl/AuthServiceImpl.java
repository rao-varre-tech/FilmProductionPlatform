package com.eee.filmplatform.services.identity.service.impl;

import java.time.Instant;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.eee.filmplatform.services.identity.dto.request.ForgotPasswordRequest;
import com.eee.filmplatform.services.identity.dto.request.LoginRequest;
import com.eee.filmplatform.services.identity.dto.request.LogoutRequest;
import com.eee.filmplatform.services.identity.dto.request.RefreshTokenRequest;
import com.eee.filmplatform.services.identity.dto.request.RegisterRequest;
import com.eee.filmplatform.services.identity.dto.request.ResetPasswordRequest;
import com.eee.filmplatform.services.identity.dto.response.LoginResponse;
import com.eee.filmplatform.services.identity.dto.response.RefreshTokenResponse;
import com.eee.filmplatform.services.identity.dto.response.UserResponse;
import com.eee.filmplatform.services.identity.entity.RefreshToken;
import com.eee.filmplatform.services.identity.entity.User;
import com.eee.filmplatform.services.identity.security.CustomUserDetails;
import com.eee.filmplatform.services.identity.security.jwt.JwtProperties;
import com.eee.filmplatform.services.identity.security.jwt.JwtService;
import com.eee.filmplatform.services.identity.service.AuthService;
import com.eee.filmplatform.services.identity.service.PasswordResetService;
import com.eee.filmplatform.services.identity.service.RefreshTokenService;
import com.eee.filmplatform.services.identity.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

        private final UserService userService;

        private final AuthenticationManager authenticationManager;

        private final JwtService jwtService;

        private final JwtProperties jwtProperties;

        private final RefreshTokenService refreshTokenService;

        private final PasswordResetService passwordResetService;

        @Override
        public UserResponse register(RegisterRequest request) {

                return userService.register(request);

        }

        @Override
        public LoginResponse login(LoginRequest request) {

                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.username(),
                                                request.password()));

                CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

                if (!user.getUser().isEmailVerified()) {
                        throw new IllegalArgumentException(
                                "Please verify your email before logging in.");
                }

                String accessToken = jwtService.generateAccessToken(user);

                String refreshToken = jwtService.generateRefreshToken(user);

                refreshTokenService.create(
                                user.getUser(),
                                refreshToken);

                return new LoginResponse(
                                accessToken,
                                refreshToken,
                                "Bearer",
                                jwtProperties.getAccessTokenExpiration());

        }

        @Override
        public RefreshTokenResponse refresh(RefreshTokenRequest request) {

                // Verify refresh token from database
                RefreshToken refreshToken = refreshTokenService.verify(request.refreshToken());

                if (refreshToken.isRevoked()) {
                        throw new IllegalArgumentException(
                                        "Refresh token has been revoked");
                }

                if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
                        throw new IllegalArgumentException(
                                        "Refresh token has expired");
                }

                User user = refreshToken.getUser();

                CustomUserDetails userDetails = new CustomUserDetails(user);

                // Revoke current refresh token
                refreshTokenService.revoke(refreshToken.getToken());

                // Generate new tokens
                String accessToken = jwtService.generateAccessToken(userDetails);

                String newRefreshToken = jwtService.generateRefreshToken(userDetails);

                // Persist new refresh token
                refreshTokenService.create(user, newRefreshToken);

                return new RefreshTokenResponse(
                                accessToken,
                                newRefreshToken,
                                "Bearer",
                                jwtProperties.getAccessTokenExpiration());
        }

        @Override
        public void logout(LogoutRequest request) {

                refreshTokenService.revoke(
                                request.refreshToken());

        }

        @Override
        public void forgotPassword(ForgotPasswordRequest request) {
                
                 passwordResetService.forgotPassword(request);
        }

        @Override
        public void resetPassword(ResetPasswordRequest request) {
                
                passwordResetService.resetPassword(request);
        }
}