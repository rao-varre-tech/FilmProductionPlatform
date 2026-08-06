package com.eee.filmplatform.services.identity.integration.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import com.eee.filmplatform.services.identity.dto.request.LoginRequest;
import com.eee.filmplatform.services.identity.dto.request.LogoutRequest;
import com.eee.filmplatform.services.identity.dto.request.RefreshTokenRequest;
import com.eee.filmplatform.services.identity.dto.request.RegisterRequest;
import com.eee.filmplatform.services.identity.entity.AuditLog;
import com.eee.filmplatform.services.identity.entity.EmailVerificationToken;
import com.eee.filmplatform.services.identity.entity.User;
import com.eee.filmplatform.services.identity.enums.AuditAction;
import com.eee.filmplatform.services.identity.integration.common.BaseIntegrationTest;
import com.eee.filmplatform.services.identity.util.TestDataFactory;

@Transactional
class AuthIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {

        String email = TestDataFactory.emailFor("john");
        String username = "john";

        RegisterRequest request = TestDataFactory.registerRequest(
                email,
                username,
                TestDataFactory.DEFAULT_PASSWORD,
                "John",
                "Doe");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.data.username").value(username));

        User user = userRepository.findByEmail(email).orElseThrow();

        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertFalse(user.isEmailVerified());

        AuditLog audit = auditLogRepository
                .findTopByUserOrderByCreatedAtDesc(user)
                .orElseThrow();

        assertEquals(AuditAction.REGISTER, audit.getAction());
    }

    @Test
    void shouldVerifyEmailSuccessfully() throws Exception {

        String email = TestDataFactory.emailFor("verify");
        String username = "verifyuser";

        registerUser(
                email,
                username,
                TestDataFactory.DEFAULT_PASSWORD,
                "Verify",
                "User");

        User user = userRepository.findByEmail(email).orElseThrow();

        EmailVerificationToken verificationToken = emailVerificationTokenRepository
                .findByUser(user)
                .orElseThrow();

        mockMvc.perform(get("/api/v1/auth/verify")
                .param("token", verificationToken.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value("Email verified successfully."));

        User verifiedUser = userRepository.findByEmail(email).orElseThrow();

        assertTrue(verifiedUser.isEmailVerified());

        EmailVerificationToken updatedToken = emailVerificationTokenRepository
                .findByToken(verificationToken.getToken())
                .orElseThrow();

        assertTrue(updatedToken.isVerified());

        AuditLog audit = auditLogRepository
                .findTopByUserOrderByCreatedAtDesc(user)
                .orElseThrow();

        assertEquals(AuditAction.EMAIL_VERIFIED, audit.getAction());
    }

    @Test
    void shouldLoginUserSuccessfullyAfterEmailVerification() throws Exception {

        String email = TestDataFactory.emailFor("login");
        String username = "loginuser";

        registerUser(
                email,
                username,
                TestDataFactory.DEFAULT_PASSWORD,
                "Login",
                "User");

        verifyUser(email);

        LoginRequest request = TestDataFactory.loginRequest(
                username,
                TestDataFactory.DEFAULT_PASSWORD);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty());
    }

    @Test
    void shouldRefreshTokenSuccessfully() throws Exception {

        String email = TestDataFactory.emailFor("refresh");
        String username = "refreshuser";

        LoginTokens tokens = registerVerifyAndLogin(
                email,
                username,
                TestDataFactory.DEFAULT_PASSWORD,
                "Refresh",
                "User");

        RefreshTokenRequest request = TestDataFactory.refreshTokenRequest(
                tokens.refreshToken());

        mockMvc.perform(post("/api/v1/auth/refresh")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty());
    }

    @Test
    void shouldLogoutUserSuccessfully() throws Exception {

        String email = TestDataFactory.emailFor("logout");
        String username = "logoutuser";

        LoginTokens tokens = registerVerifyAndLogin(
                email,
                username,
                TestDataFactory.DEFAULT_PASSWORD,
                "Logout",
                "User");

        LogoutRequest request = TestDataFactory.logoutRequest(
                tokens.refreshToken());

        mockMvc.perform(post("/api/v1/auth/logout")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
