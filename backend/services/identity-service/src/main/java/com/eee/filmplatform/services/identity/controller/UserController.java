package com.eee.filmplatform.services.identity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eee.filmplatform.services.identity.common.response.ApiResponse;
import com.eee.filmplatform.services.identity.dto.request.ChangePasswordRequest;
import com.eee.filmplatform.services.identity.dto.request.UpdateUserRequest;
import com.eee.filmplatform.services.identity.dto.response.UserResponse;
import com.eee.filmplatform.services.identity.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Users", description = "User profile management APIs")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get current user profile", description = "Retrieve the authenticated user's profile information.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User profile retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/me")
    public ApiResponse<UserResponse> getCurrentUser() {

        return ApiResponse.success(
                "User profile retrieved successfully",
                userService.getCurrentUser());
    }

    @Operation(summary = "Update current user profile", description = "Update the authenticated user's first and last name.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid profile update payload", content = @Content)
    })
    @PutMapping("/me")
    public ApiResponse<UserResponse> updateProfile(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated profile information", required = true, content = @Content(schema = @Schema(implementation = UpdateUserRequest.class))) UpdateUserRequest request) {

        return ApiResponse.success(
                "Profile updated successfully",
                userService.updateProfile(request));
    }

    @Operation(summary = "Change password", description = "Change the authenticated user's password.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password changed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid password change payload", content = @Content)
    })
    @PatchMapping("/password")
    public ApiResponse<Void> changePassword(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Current and new password values", required = true, content = @Content(schema = @Schema(implementation = ChangePasswordRequest.class))) ChangePasswordRequest request) {

        userService.changePassword(request);

        return ApiResponse.success(
                "Password changed successfully",
                null);
    }
}