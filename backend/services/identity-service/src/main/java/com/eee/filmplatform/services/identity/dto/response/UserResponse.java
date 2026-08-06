package com.eee.filmplatform.services.identity.dto.response;

import java.util.UUID;

import com.eee.filmplatform.services.identity.enums.UserStatus;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponse(

                @Schema(description = "Unique user identifier", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") UUID id,

                @Schema(description = "Email address", example = "john.doe@example.com") String email,

                @Schema(description = "Username", example = "johndoe") String username,

                @Schema(description = "First name", example = "John") String firstName,

                @Schema(description = "Last name", example = "Doe") String lastName,

                @Schema(description = "Current account status", example = "ACTIVE") UserStatus status

) {
}