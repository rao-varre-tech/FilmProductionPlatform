package com.eee.filmplatform.services.identity.service;

import com.eee.filmplatform.services.identity.dto.request.ChangePasswordRequest;
import com.eee.filmplatform.services.identity.dto.request.RegisterRequest;
import com.eee.filmplatform.services.identity.dto.request.UpdateUserRequest;
import com.eee.filmplatform.services.identity.dto.response.UserResponse;
import com.eee.filmplatform.services.identity.entity.User;

public interface UserService {

    UserResponse register(RegisterRequest request);

    UserResponse getCurrentUser();

    UserResponse updateProfile(UpdateUserRequest request);

    void changePassword(ChangePasswordRequest request);

    void loginSucceeded(User user);

    void unlockAccount(User user);

    void loginFailed(String usernameOrEmail);

}