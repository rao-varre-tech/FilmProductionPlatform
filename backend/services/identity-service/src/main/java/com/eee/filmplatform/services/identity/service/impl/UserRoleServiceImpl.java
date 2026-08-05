package com.eee.filmplatform.services.identity.service.impl;

import com.eee.filmplatform.services.identity.common.exception.ResourceNotFoundException;
import com.eee.filmplatform.services.identity.entity.Role;
import com.eee.filmplatform.services.identity.entity.User;
import com.eee.filmplatform.services.identity.entity.UserRole;
import com.eee.filmplatform.services.identity.enums.RoleName;
import com.eee.filmplatform.services.identity.repository.RoleRepository;
import com.eee.filmplatform.services.identity.repository.UserRoleRepository;
import com.eee.filmplatform.services.identity.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    private final RoleRepository roleRepository;

    private final UserRoleRepository userRoleRepository;

    @Override
    public void assignRole(User user) {

        Role defaultRole = roleRepository.findByName(RoleName.USER)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Default role not found"));

        assignRole(user, defaultRole);
    }

    @Override
    @Transactional
    public void assignRole(User user, Role role) {

        UserRole userRole = UserRole.builder()
                .user(user)
                .role(role)
                .build();

        userRoleRepository.save(userRole);
    }
}