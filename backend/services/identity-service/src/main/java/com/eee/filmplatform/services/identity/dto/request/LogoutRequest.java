package com.eee.filmplatform.services.identity.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequest(

        @NotBlank String refreshToken

) {
}