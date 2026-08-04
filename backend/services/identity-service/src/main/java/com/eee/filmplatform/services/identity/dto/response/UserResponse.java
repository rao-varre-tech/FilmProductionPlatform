package com.eee.filmplatform.services.identity.dto.response;

import com.eee.filmplatform.services.identity.enums.UserStatus;

import java.util.UUID;

public record UserResponse(

        UUID id,

        String email,

        String username,

        String firstName,

        String lastName,

        UserStatus status

) {
}