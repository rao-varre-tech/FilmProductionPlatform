package com.eee.filmplatform.services.identity.common.exception;

import java.time.Instant;
import java.util.List;

import lombok.Builder;

@Builder
public record ErrorResponse(

        Instant timestamp,

        int status,

        String error,

        String message,

        String path,

        List<String> details

) {
}