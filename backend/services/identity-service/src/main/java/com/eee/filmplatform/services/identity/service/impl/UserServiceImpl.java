package com.eee.filmplatform.services.identity.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eee.filmplatform.services.identity.common.exception.ResourceNotFoundException;
import com.eee.filmplatform.services.identity.config.SecurityProperties;
import com.eee.filmplatform.services.identity.dto.request.ChangePasswordRequest;
import com.eee.filmplatform.services.identity.dto.request.RegisterRequest;
import com.eee.filmplatform.services.identity.dto.request.UpdateUserRequest;
import com.eee.filmplatform.services.identity.dto.response.UserResponse;
import com.eee.filmplatform.services.identity.entity.User;
import com.eee.filmplatform.services.identity.enums.UserStatus;
import com.eee.filmplatform.services.identity.mapper.UserMapper;
import com.eee.filmplatform.services.identity.repository.UserRepository;
import com.eee.filmplatform.services.identity.security.CustomUserDetails;
import com.eee.filmplatform.services.identity.service.EmailVerificationService;
import com.eee.filmplatform.services.identity.service.RefreshTokenService;
import com.eee.filmplatform.services.identity.service.UserRoleService;
import com.eee.filmplatform.services.identity.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final UserRoleService userRoleService;

    private final EmailVerificationService emailVerificationService;

    private final RefreshTokenService refreshTokenService;

    private final SecurityProperties securityProperties;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {

        validateRegistration(request);

        User user = buildUser(request);

        User savedUser = userRepository.save(user);

        userRoleService.assignRole(savedUser);

        emailVerificationService.create(savedUser);

        return userMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse getCurrentUser() {

        User user = getAuthenticatedUser();

        System.out.println("id: " + user.getId());
        System.out.println("email: " + user.getEmail());
        System.out.println("username: " + user.getUsername());
        System.out.println("first name: " + user.getFirstName());
        System.out.println("last name: " + user.getLastName());
        System.out.println("status: " + user.getStatus());

        UserResponse userResponse = userMapper.toResponse(user);

        System.out.println("User Response: " + userResponse);

        return userResponse;
    }

    @Override
    @Transactional
    public UserResponse updateProfile(UpdateUserRequest request) {

        User user = getAuthenticatedUser();

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());

        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {

        User user = getAuthenticatedUser();
        if (!passwordEncoder.matches(
                request.currentPassword(),
                user.getPassword())) {

            throw new IllegalArgumentException(
                    "Current password is incorrect.");
        }
        user.setPassword(
                passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        refreshTokenService.revokeAll(user);
    }

    @Override
    @Transactional
    public void loginSucceeded(User user) {

        user.setFailedLoginAttempts(0);
        user.setAccountLockedUntil(null);
        user.setAccountNonLocked(true);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void unlockAccount(User user) {

        user.setAccountNonLocked(true);
        user.setAccountLockedUntil(null);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void loginFailed(String usernameOrEmail) {

        userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .ifPresent(user -> {

                    int attempts = user.getFailedLoginAttempts() + 1;

                    user.setFailedLoginAttempts(attempts);

                    if (attempts >= securityProperties.getMaxFailedAttempts()) {

                        user.setAccountNonLocked(false);

                        user.setAccountLockedUntil(
                                Instant.now().plus(
                                        securityProperties.getLockDurationMinutes(),
                                        ChronoUnit.MINUTES));
                    }

                    userRepository.save(user);
                });
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

    private User getAuthenticatedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}