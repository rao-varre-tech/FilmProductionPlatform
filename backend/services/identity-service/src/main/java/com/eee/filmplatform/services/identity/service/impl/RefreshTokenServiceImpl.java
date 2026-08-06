package com.eee.filmplatform.services.identity.service.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.eee.filmplatform.services.identity.common.exception.InvalidTokenException;
import com.eee.filmplatform.services.identity.common.exception.ResourceNotFoundException;
import com.eee.filmplatform.services.identity.entity.RefreshToken;
import com.eee.filmplatform.services.identity.entity.User;
import com.eee.filmplatform.services.identity.enums.AuditAction;
import com.eee.filmplatform.services.identity.enums.AuditStatus;
import com.eee.filmplatform.services.identity.repository.RefreshTokenRepository;
import com.eee.filmplatform.services.identity.security.jwt.JwtProperties;
import com.eee.filmplatform.services.identity.service.AuditService;
import com.eee.filmplatform.services.identity.service.RefreshTokenService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtProperties jwtProperties;

    private final AuditService auditService;

    @Override
    public RefreshToken create(User user, String token) {

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expiresAt(
                        Instant.now()
                                .plusMillis(jwtProperties.getRefreshTokenExpiration()))
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verify(String token) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found"));

        if (refreshToken.isRevoked()) {
            throw new InvalidTokenException("Refresh token has expired");
        }

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidTokenException("Refresh token has expired");
        }

        return refreshToken;
    }

    @Override
    public void revoke(String token) {

        RefreshToken refreshToken = verify(token);

        refreshToken.setRevoked(true);

        refreshTokenRepository.save(refreshToken);

        User user = refreshToken.getUser();

        auditService.log(
                AuditAction.LOGOUT,
                AuditStatus.SUCCESS,
                user,
                user.getUsername(),
                "User logged out successfully.");
    }

    @Override
    @Transactional
    public void revokeAll(User user) {

        List<RefreshToken> tokens = refreshTokenRepository.findByUserAndRevokedFalse(user);

        tokens.forEach(token -> token.setRevoked(true));

        refreshTokenRepository.saveAll(tokens);
    }
}