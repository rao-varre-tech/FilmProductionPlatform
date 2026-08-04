package com.eee.filmplatform.services.identity.service.impl;

import com.eee.filmplatform.services.identity.dto.request.LoginRequest;
import com.eee.filmplatform.services.identity.dto.request.RegisterRequest;
import com.eee.filmplatform.services.identity.dto.response.LoginResponse;
import com.eee.filmplatform.services.identity.dto.response.UserResponse;
import com.eee.filmplatform.services.identity.service.AuthService;
import com.eee.filmplatform.services.identity.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    @Override
    public UserResponse register(RegisterRequest request) {

        return userService.register(request);

    }

    @Override
    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.username(),
                    request.password()
            )
        );

        return new LoginResponse("Login successful");

    }
}