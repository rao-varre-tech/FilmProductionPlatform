package com.eee.filmplatform.services.identity.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(

                @Schema(description = "Access token used for authenticated requests", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9") String accessToken,

                @Schema(description = "Refresh token used to obtain a new access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9") String refreshToken,

                @Schema(description = "Token type", example = "Bearer") String tokenType,

                @Schema(description = "Token expiration time in seconds", example = "3600") long expiresIn

) {
}