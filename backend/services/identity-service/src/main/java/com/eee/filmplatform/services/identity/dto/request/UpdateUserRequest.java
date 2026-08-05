package com.eee.filmplatform.services.identity.dto.request;

import jakarta.validation.constraints.*;

public record UpdateUserRequest(

        @NotBlank
        @Size(max = 100)
        String firstName,

        @NotBlank
        @Size(max = 100)
        String lastName

) {
}
