package com.eee.filmplatform.services.identity.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eee.filmplatform.services.identity.common.exception.InvalidTokenException;
import com.eee.filmplatform.services.identity.common.exception.ResourceNotFoundException;
import com.eee.filmplatform.services.identity.dto.request.ForgotPasswordRequest;
import com.eee.filmplatform.services.identity.dto.request.ResetPasswordRequest;
import com.eee.filmplatform.services.identity.entity.PasswordResetToken;
import com.eee.filmplatform.services.identity.entity.RefreshToken;
import com.eee.filmplatform.services.identity.entity.User;
import com.eee.filmplatform.services.identity.enums.AuditAction;
import com.eee.filmplatform.services.identity.enums.AuditStatus;
import com.eee.filmplatform.services.identity.repository.PasswordResetTokenRepository;
import com.eee.filmplatform.services.identity.repository.RefreshTokenRepository;
import com.eee.filmplatform.services.identity.repository.UserRepository;
import com.eee.filmplatform.services.identity.service.AuditService;
import com.eee.filmplatform.services.identity.service.EmailService;
import com.eee.filmplatform.services.identity.service.PasswordResetService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

        private final PasswordResetTokenRepository passwordResetTokenRepository;

        private final UserRepository userRepository;

        private final PasswordEncoder passwordEncoder;

        private final RefreshTokenRepository refreshTokenRepository;

        private final EmailService emailService;

        private final AuditService auditService;

        @Override
        @Transactional
        public void forgotPassword(ForgotPasswordRequest request) {

                Optional<User> optionalUser = userRepository.findByEmail(request.email());

                // Prevent user enumeration
                if (optionalUser.isEmpty()) {
                        return;
                }

                User user = optionalUser.get();

                List<PasswordResetToken> tokens = passwordResetTokenRepository.findByUserAndUsedFalse(user);

                tokens.forEach(token -> token.setUsed(true));

                passwordResetTokenRepository.saveAll(tokens);

                PasswordResetToken token = PasswordResetToken.builder()
                                .token(UUID.randomUUID().toString())
                                .user(user)
                                .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                                .used(false)
                                .build();

                PasswordResetToken savedToken = passwordResetTokenRepository.save(token);

                System.out.println();
                System.out.println("========================================");
                System.out.println("Password Reset URL");
                System.out.println(
                                "http://localhost:8081/api/v1/auth/reset-password?token="
                                                + savedToken.getToken());
                System.out.println("========================================");
                System.out.println();

                String verificationUrl = "http://localhost:8081/api/v1/auth/reset-password?token="
                                + savedToken.getToken();
                emailService.sendTextEmail(user.getEmail(),
                                "Reset password for your Film Platform account",
                                """
                                                You have requested to reset your password for your Film Platform account.

                                                Please click the link below to reset your password:

                                                %s

                                                This link will expire in 1 hour.

                                                If you did not request a password reset, you can safely ignore this email.
                                                """
                                                .formatted(verificationUrl));

                auditService.log(
                                AuditAction.PASSWORD_RESET_REQUESTED,
                                AuditStatus.SUCCESS,
                                user,
                                user.getUsername(),
                                "Password reset requested.");
        }

        @Override
        @Transactional
        public void resetPassword(ResetPasswordRequest request) {

                PasswordResetToken token = passwordResetTokenRepository
                                .findByToken(request.token())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Password reset token not found"));

                if (token.isUsed()) {
                        throw new InvalidTokenException(
                                        "Password reset token has already been used.");
                }

                if (token.getExpiresAt().isBefore(Instant.now())) {
                        throw new InvalidTokenException(
                                        "Password reset token has expired.");
                }

                User user = token.getUser();

                user.setPassword(
                                passwordEncoder.encode(request.newPassword()));

                List<RefreshToken> refreshTokens = refreshTokenRepository
                                .findByUserAndRevokedFalse(user);

                refreshTokens.forEach(tkn -> tkn.setRevoked(true));

                refreshTokenRepository.saveAll(refreshTokens);

                userRepository.save(user);

                token.setUsed(true);

                passwordResetTokenRepository.save(token);

                auditService.log(
                                AuditAction.PASSWORD_RESET_COMPLETED,
                                AuditStatus.SUCCESS,
                                user,
                                user.getUsername(),
                                "Password reset completed.");
        }

}
