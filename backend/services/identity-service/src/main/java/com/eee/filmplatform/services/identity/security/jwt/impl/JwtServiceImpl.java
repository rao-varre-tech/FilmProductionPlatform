package com.eee.filmplatform.services.identity.security.jwt.impl;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.eee.filmplatform.services.identity.security.CustomUserDetails;
import com.eee.filmplatform.services.identity.security.jwt.JwtProperties;
import com.eee.filmplatform.services.identity.security.jwt.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;

    @Override
    public String generateAccessToken(CustomUserDetails user) {

        return generateAccessToken(Map.of(), user);
    }

    @Override
    public String generateAccessToken(
            Map<String, Object> extraClaims,
            CustomUserDetails user) {

        return buildToken(
                extraClaims,
                user,
                jwtProperties.getAccessTokenExpiration());
    }

    @Override
    public String generateRefreshToken(CustomUserDetails user) {

        return buildToken(
                Map.of(),
                user,
                jwtProperties.getRefreshTokenExpiration());
    }

    @Override
    public String extractUsername(String token) {

        return extractClaim(
                token,
                Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver) {

        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claimsResolver.apply(claims);
    }

    @Override
    public boolean isTokenValid(
            String token,
            CustomUserDetails user) {

        String username = extractUsername(token);

        return username.equals(user.getUsername())
                && !isTokenExpired(token);
    }

    @Override
    public boolean isTokenExpired(String token) {

        return extractClaim(
                token,
                Claims::getExpiration)
                .before(new Date());
    }

    private SecretKey getSigningKey() {

        System.out.println("Secret = " + jwtProperties.getSecret());

        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());

        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            CustomUserDetails user,
            long expiration) {

        Instant now = Instant.now();

        return Jwts.builder()
                .claims(new HashMap<>(extraClaims))
                .id(UUID.randomUUID().toString())
                .subject(user.getUsername())
                .issuer(jwtProperties.getIssuer())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expiration)))
                .signWith(getSigningKey())
                .compact();
    }
}