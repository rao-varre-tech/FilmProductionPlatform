package com.eee.filmplatform.services.identity.integration.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.eee.filmplatform.services.identity.dto.request.ChangePasswordRequest;
import com.eee.filmplatform.services.identity.dto.request.LoginRequest;
import com.eee.filmplatform.services.identity.dto.request.UpdateUserRequest;
import com.eee.filmplatform.services.identity.entity.AuditLog;
import com.eee.filmplatform.services.identity.entity.User;
import com.eee.filmplatform.services.identity.enums.AuditAction;
import com.eee.filmplatform.services.identity.integration.common.BaseIntegrationTest;
import com.eee.filmplatform.services.identity.util.TestDataFactory;

@Transactional
class UserIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldGetCurrentUserProfileSuccessfully() throws Exception {

        String email = TestDataFactory.emailFor("profile");
        String username = "profileuser";

        registerVerifyAndLogin(
                email,
                username,
                TestDataFactory.DEFAULT_PASSWORD,
                "Profile",
                "User");

        String token = authorizationHeaderFor(
                username,
                TestDataFactory.DEFAULT_PASSWORD);

        mockMvc.perform(get("/api/v1/users/me")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.data.username").value(username))
                .andExpect(jsonPath("$.data.firstName").value("Profile"))
                .andExpect(jsonPath("$.data.lastName").value("User"));
    }

    @Test
    void shouldUpdateUserProfileSuccessfully() throws Exception {

        String email = TestDataFactory.emailFor("update");
        String username = "updateuser";

        LoginTokens tokens = registerVerifyAndLogin(
                email,
                username,
                TestDataFactory.DEFAULT_PASSWORD,
                "Profile",
                "User");

        String token = bearerToken(tokens);

        UpdateUserRequest request = TestDataFactory.updateUserRequest(
                "Updated",
                "Name");

        mockMvc.perform(put("/api/v1/users/me")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.firstName").value("Updated"))
                .andExpect(jsonPath("$.data.lastName").value("Name"));

        User updatedUser = userRepository.findByEmail(email)
                .orElseThrow();

        assertEquals("Updated", updatedUser.getFirstName());
        assertEquals("Name", updatedUser.getLastName());

        AuditLog audit = auditLogRepository
                .findTopByUserOrderByCreatedAtDesc(updatedUser)
                .orElseThrow();

        assertEquals(
                AuditAction.PROFILE_UPDATED,
                audit.getAction());
    }

    @Test
    void shouldChangePasswordSuccessfully() throws Exception {

        String email = TestDataFactory.emailFor("changepass");
        String username = "changepass";
        String oldPassword = TestDataFactory.DEFAULT_PASSWORD;
        String newPassword = TestDataFactory.DEFAULT_NEW_PASSWORD;

        LoginTokens tokens = registerVerifyAndLogin(
                email,
                username,
                oldPassword,
                "Change",
                "Password");

        String token = bearerToken(tokens);

        ChangePasswordRequest request = TestDataFactory.changePasswordRequest();

        mockMvc.perform(patch("/api/v1/users/password")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        User updatedUser = userRepository.findByEmail(email)
                .orElseThrow();

        assertTrue(passwordEncoder.matches(
                newPassword,
                updatedUser.getPassword()));

        LoginRequest oldLogin = TestDataFactory.loginRequest(
                username,
                oldPassword);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(oldLogin)))
                .andExpect(status().isUnauthorized());

        LoginRequest newLogin = TestDataFactory.loginRequest(
                username,
                newPassword);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newLogin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty());

        var auditLogs = auditLogRepository.findAll().stream()
                .filter(audit -> audit.getUser() != null
                        && audit.getUser().getId().equals(updatedUser.getId()))
                .toList();

        assertTrue(auditLogs.stream()
                .anyMatch(audit -> audit.getAction() == AuditAction.PASSWORD_CHANGED));
    }
}
