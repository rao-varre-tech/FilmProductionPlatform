package com.eee.filmplatform.services.identity.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eee.filmplatform.services.identity.dto.request.RegisterRequest;
import com.eee.filmplatform.services.identity.dto.response.UserResponse;
import com.eee.filmplatform.services.identity.entity.User;
import com.eee.filmplatform.services.identity.enums.UserStatus;
import com.eee.filmplatform.services.identity.mapper.UserMapper;
import com.eee.filmplatform.services.identity.repository.RoleRepository;
import com.eee.filmplatform.services.identity.repository.UserRepository;
import com.eee.filmplatform.services.identity.service.UserRoleService;
import com.eee.filmplatform.services.identity.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final UserRoleService userRoleService;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {

        validateRegistration(request);

        User user = buildUser(request);

        User savedUser = userRepository.save(user);

        userRoleService.assignRole(savedUser);

        return userMapper.toResponse(savedUser);
    }

    private void validateRegistration(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }
    }

    private User buildUser(RegisterRequest request) {

        User user = new User();

        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());

        user.setStatus(UserStatus.ACTIVE);

        user.setEnabled(true);
        user.setEmailVerified(false);
        user.setAccountNonLocked(true);

        return user;
    }
}