package com.eee.filmplatform.services.identity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LogoutRequest(

                @Schema(description = "Refresh token to invalidate during logout", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank String refreshToken

) {
}