package com.eee.filmplatform.services.identity.util;

import com.eee.filmplatform.services.identity.dto.request.ChangePasswordRequest;
import com.eee.filmplatform.services.identity.dto.request.ForgotPasswordRequest;
import com.eee.filmplatform.services.identity.dto.request.LoginRequest;
import com.eee.filmplatform.services.identity.dto.request.LogoutRequest;
import com.eee.filmplatform.services.identity.dto.request.RefreshTokenRequest;
import com.eee.filmplatform.services.identity.dto.request.RegisterRequest;
import com.eee.filmplatform.services.identity.dto.request.ResetPasswordRequest;
import com.eee.filmplatform.services.identity.dto.request.UpdateUserRequest;

public final class TestDataFactory {

    public static final String DEFAULT_PASSWORD = "Password@123";
    public static final String DEFAULT_NEW_PASSWORD = "NewPassword@123";
    public static final String DEFAULT_FIRST_NAME = "Test";
    public static final String DEFAULT_LAST_NAME = "User";

    private TestDataFactory() {
    }

    public static RegisterRequest registerRequest(String email, String username, String password, String firstName,
            String lastName) {
        return new RegisterRequest(email, username, password, firstName, lastName);
    }

    public static RegisterRequest registerRequest(String email, String username, String firstName, String lastName) {
        return registerRequest(email, username, DEFAULT_PASSWORD, firstName, lastName);
    }

    public static LoginRequest loginRequest(String username, String password) {
        return new LoginRequest(username, password);
    }

    public static LoginRequest loginRequest(String username) {
        return loginRequest(username, DEFAULT_PASSWORD);
    }

    public static RefreshTokenRequest refreshTokenRequest(String token) {
        return new RefreshTokenRequest(token);
    }

    public static LogoutRequest logoutRequest(String token) {
        return new LogoutRequest(token);
    }

    public static ForgotPasswordRequest forgotPasswordRequest(String email) {
        return new ForgotPasswordRequest(email);
    }

    public static ResetPasswordRequest resetPasswordRequest(String token) {
        return new ResetPasswordRequest(token, DEFAULT_NEW_PASSWORD);
    }

    public static ChangePasswordRequest changePasswordRequest() {
        return new ChangePasswordRequest(DEFAULT_PASSWORD, DEFAULT_NEW_PASSWORD);
    }

    public static ChangePasswordRequest changePasswordRequest(String currentPassword, String newPassword) {
        return new ChangePasswordRequest(currentPassword, newPassword);
    }

    public static UpdateUserRequest updateUserRequest(String firstName, String lastName) {
        return new UpdateUserRequest(firstName, lastName);
    }

    public static String emailFor(String prefix) {
        return prefix + "@example.com";
    }
}
