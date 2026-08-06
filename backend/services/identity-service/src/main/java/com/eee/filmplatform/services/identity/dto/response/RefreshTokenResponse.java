package com.eee.filmplatform.services.identity.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record RefreshTokenResponse(

                @Schema(description = "New access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9") String accessToken,

                @Schema(description = "Refresh token returned with the new access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9") String refreshToken,

                @Schema(description = "Token type", example = "Bearer") String tokenType,

                @Schema(description = "Expiration time in seconds", example = "3600") long expiresIn

) {
}