package com.eee.filmplatform.services.identity.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eee.filmplatform.services.identity.common.exception.ResourceNotFoundException;
import com.eee.filmplatform.services.identity.entity.EmailVerificationToken;
import com.eee.filmplatform.services.identity.entity.User;
import com.eee.filmplatform.services.identity.repository.EmailVerificationTokenRepository;
import com.eee.filmplatform.services.identity.repository.UserRepository;
import com.eee.filmplatform.services.identity.service.EmailService;
import com.eee.filmplatform.services.identity.service.EmailVerificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl
                implements EmailVerificationService {

        private final EmailVerificationTokenRepository tokenRepository;

        private final UserRepository userRepository;

        private final EmailService emailService;

        @Override
        @Transactional
        public EmailVerificationToken create(User user) {

                EmailVerificationToken token = EmailVerificationToken.builder()
                                .token(UUID.randomUUID().toString())
                                .user(user)
                                .expiresAt(Instant.now().plus(24, ChronoUnit.HOURS))
                                .verified(false)
                                .build();

                EmailVerificationToken savedToken = tokenRepository.save(token);

                // Temporary until SMTP is configured
                System.out.println();
                System.out.println("==========================================");
                System.out.println("Email Verification URL");
                System.out.println(
                                "http://localhost:8081/api/v1/auth/verify?token="
                                                + savedToken.getToken());
                System.out.println("==========================================");
                System.out.println();
                String verificationUrl = "http://localhost:8081/api/v1/auth/verify?token="
                                + savedToken.getToken();
                String html = """
                                <html>
                                <body style="font-family:Arial,sans-serif">

                                <h2>Welcome to Film Production Platform</h2>

                                <p>
                                Thank you for registering.
                                </p>

                                <p>
                                Click the button below to verify your email.
                                </p>

                                <p>
                                <a href="%s"
                                style="
                                background:#2563eb;
                                color:white;
                                padding:12px 24px;
                                text-decoration:none;
                                border-radius:6px;">
                                Verify Email
                                </a>
                                </p>

                                <p>
                                This link expires in 24 hours.
                                </p>

                                </body>
                                </html>
                                """.formatted(verificationUrl);
                emailService.sendHtmlEmail(user.getEmail(),
                                "Verify your Film Platform account",
                                html);

                return savedToken;
        }

        @Override
        @Transactional
        public String verify(String token) {

                EmailVerificationToken verificationToken = tokenRepository.findByToken(token)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Verification token not found"));

                if (verificationToken.isVerified()) {
                        throw new IllegalArgumentException(
                                        "Email already verified");
                }

                if (verificationToken.getExpiresAt().isBefore(Instant.now())) {
                        throw new IllegalArgumentException(
                                        "Verification token expired");
                }

                User user = verificationToken.getUser();

                user.setEmailVerified(true);

                userRepository.save(user);

                verificationToken.setVerified(true);

                tokenRepository.save(verificationToken);

                return "Email verified successfully.";
        }
}
