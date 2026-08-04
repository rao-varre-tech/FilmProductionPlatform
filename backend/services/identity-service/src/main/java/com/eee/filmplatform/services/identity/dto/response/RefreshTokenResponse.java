package com.eee.filmplatform.services.identity.dto.response;

public record RefreshTokenResponse(

        String accessToken,

        String refreshToken,

        String tokenType,

        long expiresIn

) {
}