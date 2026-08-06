package com.eee.filmplatform.services.identity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

                @Schema(description = "Username or email used to authenticate", example = "johndoe", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank String username,

                @Schema(description = "Password for the account", example = "StrongPass123!", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank String password

) {
}