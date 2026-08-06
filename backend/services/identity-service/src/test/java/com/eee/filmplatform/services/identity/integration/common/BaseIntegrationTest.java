package com.eee.filmplatform.services.identity.integration.common;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.eee.filmplatform.services.identity.dto.request.LoginRequest;
import com.eee.filmplatform.services.identity.dto.request.RegisterRequest;
import com.eee.filmplatform.services.identity.entity.EmailVerificationToken;
import com.eee.filmplatform.services.identity.entity.User;
import com.eee.filmplatform.services.identity.repository.AuditLogRepository;
import com.eee.filmplatform.services.identity.repository.EmailVerificationTokenRepository;
import com.eee.filmplatform.services.identity.repository.UserRepository;
import com.eee.filmplatform.services.identity.service.EmailService;
import com.eee.filmplatform.services.identity.util.TestDataFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected AuditLogRepository auditLogRepository;

    @Autowired
    protected EmailVerificationTokenRepository emailVerificationTokenRepository;

    @MockitoBean
    protected EmailService emailService;

    /**
     * Holds authentication tokens returned by the login endpoint.
     */
    protected record LoginTokens(
            String accessToken,
            String refreshToken) {
    }

    protected User registerUser(
            String email,
            String username,
            String password,
            String firstName,
            String lastName) throws Exception {

        RegisterRequest request = TestDataFactory.registerRequest(
                email,
                username,
                password,
                firstName,
                lastName);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        return userRepository.findByEmail(email)
                .orElseThrow();
    }

    protected User verifyUser(String email) throws Exception {

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        EmailVerificationToken verificationToken = emailVerificationTokenRepository
                .findByUser(user)
                .orElseThrow();

        mockMvc.perform(get("/api/v1/auth/verify")
                .param("token", verificationToken.getToken()))
                .andExpect(status().isOk());

        return userRepository.findByEmail(email)
                .orElseThrow();
    }

    protected LoginTokens login(
            String username,
            String password) throws Exception {

        LoginRequest request = TestDataFactory.loginRequest(username, password);

        String responseBody = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode response = objectMapper.readTree(responseBody);

        return new LoginTokens(
                response.path("data").path("accessToken").asText(),
                response.path("data").path("refreshToken").asText());
    }

    protected String authorizationHeaderFor(
            String username,
            String password) throws Exception {

        return "Bearer " + login(username, password).accessToken();
    }

    /**
     * Convenience helper for tests that need a fully authenticated user.
     */
    protected LoginTokens registerVerifyAndLogin(
            String email,
            String username,
            String password,
            String firstName,
            String lastName) throws Exception {

        registerUser(
                email,
                username,
                password,
                firstName,
                lastName);

        verifyUser(email);

        return login(username, password);
    }

    protected String bearerToken(LoginTokens tokens) {
        return "Bearer " + tokens.accessToken();
    }
}
