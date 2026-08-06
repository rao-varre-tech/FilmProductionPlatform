package com.eee.filmplatform.services.identity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(

                @Schema(description = "Updated first name", example = "Jane", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank @Size(max = 100) String firstName,

                @Schema(description = "Updated last name", example = "Smith", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank @Size(max = 100) String lastName

) {
}
