package com.eee.filmplatform.services.identity.service.impl;

import com.eee.filmplatform.services.identity.entity.Role;
import com.eee.filmplatform.services.identity.entity.User;
import com.eee.filmplatform.services.identity.entity.UserRole;
import com.eee.filmplatform.services.identity.repository.UserRoleRepository;
import com.eee.filmplatform.services.identity.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    @Override
    public void assignRole(User user, Role role) {

        UserRole userRole = UserRole.builder()
                .user(user)
                .role(role)
                .build();

        userRoleRepository.save(userRole);
    }
}