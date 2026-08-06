package com.eee.filmplatform.services.identity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(

                @Schema(description = "Password reset token received by email", example = "reset-token-123", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank String token,

                @Schema(description = "New password to set for the account", example = "NewStrongPass123!", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank @Size(min = 8, max = 100) String newPassword

) {
}
