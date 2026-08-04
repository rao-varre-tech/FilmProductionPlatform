package com.eee.filmplatform.services.identity.common.response;

import java.time.Instant;

import lombok.Builder;

@Builder
public record ApiResponse<T>(
                boolean success,
                String message,
                T data,
                Instant timestamp) {

        public static <T> ApiResponse<T> success(T data, String message) {
                return ApiResponse.<T>builder()
                                .success(true)
                                .message(message)
                                .data(data)
                                .timestamp(Instant.now())
                                .build();
        }

        public static <T> ApiResponse<T> failure(String message) {
                return ApiResponse.<T>builder()
                                .success(false)
                                .message(message)
                                .timestamp(Instant.now())
                                .build();
        }
}