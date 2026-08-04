package com.eee.filmplatform.services.identity.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(

        @NotBlank String refreshToken

) {
}