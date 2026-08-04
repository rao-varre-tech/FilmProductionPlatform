package com.eee.filmplatform.services.identity.service;

import com.eee.filmplatform.services.identity.dto.request.RegisterRequest;
import com.eee.filmplatform.services.identity.dto.response.UserResponse;

public interface UserService {

    UserResponse register(RegisterRequest request);

}