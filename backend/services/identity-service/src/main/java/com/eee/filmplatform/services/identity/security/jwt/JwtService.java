package com.eee.filmplatform.services.identity.security.jwt;

import com.eee.filmplatform.services.identity.security.CustomUserDetails;
import io.jsonwebtoken.Claims;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    String generateAccessToken(CustomUserDetails user);

    String generateAccessToken(
            Map<String, Object> extraClaims,
            CustomUserDetails user);

    String generateRefreshToken(CustomUserDetails user);

    String extractUsername(String token);

    <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver);

    boolean isTokenValid(
            String token,
            CustomUserDetails user);

    boolean isTokenExpired(String token);
}