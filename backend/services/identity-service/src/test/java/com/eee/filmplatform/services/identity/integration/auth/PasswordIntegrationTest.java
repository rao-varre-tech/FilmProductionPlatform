package com.eee.filmplatform.services.identity.integration.auth;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.eee.filmplatform.services.identity.dto.request.ForgotPasswordRequest;
import com.eee.filmplatform.services.identity.dto.request.LoginRequest;
import com.eee.filmplatform.services.identity.dto.request.ResetPasswordRequest;
import com.eee.filmplatform.services.identity.entity.AuditLog;
import com.eee.filmplatform.services.identity.entity.PasswordResetToken;
import com.eee.filmplatform.services.identity.entity.User;
import com.eee.filmplatform.services.identity.enums.AuditAction;
import com.eee.filmplatform.services.identity.integration.common.BaseIntegrationTest;
import com.eee.filmplatform.services.identity.repository.PasswordResetTokenRepository;
import com.eee.filmplatform.services.identity.util.TestDataFactory;

@Transactional
class PasswordIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldRequestPasswordResetSuccessfully() throws Exception {

        String email = TestDataFactory.emailFor("resetreq");
        String username = "resetreq";

        registerUser(
                email,
                username,
                TestDataFactory.DEFAULT_PASSWORD,
                "Reset",
                "Request");

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        ForgotPasswordRequest request = TestDataFactory.forgotPasswordRequest(email);

        mockMvc.perform(post("/api/v1/auth/forgot-password")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value(containsString("password reset")));

        PasswordResetToken resetToken = passwordResetTokenRepository
                .findByUserAndUsedFalse(user)
                .stream()
                .findFirst()
                .orElseThrow();

        assertNotNull(resetToken);
        assertFalse(resetToken.isUsed());
        assertEquals(user.getId(), resetToken.getUser().getId());

        AuditLog auditLog = auditLogRepository
                .findTopByUserOrderByCreatedAtDesc(user)
                .orElseThrow();

        assertEquals(
                AuditAction.PASSWORD_RESET_REQUESTED,
                auditLog.getAction());
    }

    @Test
    void shouldResetPasswordSuccessfully() throws Exception {

        String email = TestDataFactory.emailFor("resetpass");
        String username = "resetpass";
        String oldPassword = TestDataFactory.DEFAULT_PASSWORD;
        String newPassword = TestDataFactory.DEFAULT_NEW_PASSWORD;

        registerUser(
                email,
                username,
                oldPassword,
                "Reset",
                "Password");

        verifyUser(email);

        // Step 1 - Request password reset
        ForgotPasswordRequest forgotPasswordRequest = TestDataFactory.forgotPasswordRequest(email);

        mockMvc.perform(post("/api/v1/auth/forgot-password")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(forgotPasswordRequest)))
                .andExpect(status().isOk());

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        PasswordResetToken resetToken = passwordResetTokenRepository
                .findByUserAndUsedFalse(user)
                .stream()
                .findFirst()
                .orElseThrow();

        // Step 2 - Reset password using the generated token
        ResetPasswordRequest resetRequest = TestDataFactory.resetPasswordRequest(
                resetToken.getToken());

        mockMvc.perform(post("/api/v1/auth/reset-password")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resetRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value("Password reset successfully."));

        // Step 3 - Verify token state
        PasswordResetToken reloadedToken = passwordResetTokenRepository
                .findByToken(resetToken.getToken())
                .orElseThrow();

        assertTrue(reloadedToken.isUsed());

        // Step 4 - Verify password changed
        User reloadedUser = userRepository
                .findById(user.getId())
                .orElseThrow();

        assertTrue(passwordEncoder.matches(
                newPassword,
                reloadedUser.getPassword()));

        assertFalse(passwordEncoder.matches(
                oldPassword,
                reloadedUser.getPassword()));

        // Step 5 - Verify audit log
        AuditLog auditLog = auditLogRepository
                .findTopByUserOrderByCreatedAtDesc(reloadedUser)
                .orElseThrow();

        assertEquals(
                AuditAction.PASSWORD_RESET_COMPLETED,
                auditLog.getAction());

        // Step 6 - Verify login with the new password
        LoginRequest loginRequest = TestDataFactory.loginRequest(
                username,
                newPassword);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty());
    }
}
