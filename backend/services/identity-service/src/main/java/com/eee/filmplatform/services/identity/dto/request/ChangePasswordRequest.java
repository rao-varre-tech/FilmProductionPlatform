package com.eee.filmplatform.services.identity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(

                @Schema(description = "Current password for verification", example = "StrongPass123!", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank String currentPassword,

                @Schema(description = "New password to set", example = "NewStrongPass123!", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank @Size(min = 8, max = 100) String newPassword

) {
}