package com.eee.filmplatform.services.identity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(

                @Schema(description = "Email address for password reset", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank @Email String email

) {
}
