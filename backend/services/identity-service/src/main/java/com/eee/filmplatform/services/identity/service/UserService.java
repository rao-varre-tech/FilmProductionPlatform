package com.eee.filmplatform.services.identity.service;

import com.eee.filmplatform.services.identity.dto.request.ChangePasswordRequest;
import com.eee.filmplatform.services.identity.dto.request.RegisterRequest;
import com.eee.filmplatform.services.identity.dto.request.UpdateUserRequest;
import com.eee.filmplatform.services.identity.dto.response.UserResponse;

public interface UserService {

    UserResponse register(RegisterRequest request);

    UserResponse getCurrentUser();

    UserResponse updateProfile(UpdateUserRequest request);

    void changePassword(ChangePasswordRequest request);

}