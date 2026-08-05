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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // @GetMapping("/me")
    // public String me(Authentication authentication) {

    // return "Hello " + authentication.getName();

    // }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getCurrentUser() {

        return ApiResponse.success(
                "User profile retrieved successfully",
                userService.getCurrentUser());
    }

    @PutMapping("/me")
    public ApiResponse<UserResponse> updateProfile(
            @Valid @RequestBody UpdateUserRequest request) {

        return ApiResponse.success(
                "Profile updated successfully",
                userService.updateProfile(request));
    }

    @PatchMapping("/password")
    public ApiResponse<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        userService.changePassword(request);

        return ApiResponse.success(
                "Password changed successfully",
                null);
    }
}