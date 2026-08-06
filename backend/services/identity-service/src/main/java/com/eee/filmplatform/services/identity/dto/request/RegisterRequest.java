package com.eee.filmplatform.services.identity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

                @Schema(description = "Email address for the new account", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank @Email String email,

                @Schema(description = "Unique username for the account", example = "johndoe", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank @Size(min = 3, max = 50) String username,

                @Schema(description = "Password for the new account", example = "StrongPass123!", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank @Size(min = 8, max = 100) String password,

                @Schema(description = "First name of the user", example = "John", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank String firstName,

                @Schema(description = "Last name of the user", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank String lastName

) {
}