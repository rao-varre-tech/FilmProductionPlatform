package com.eee.filmplatform.services.identity.service;

import com.eee.filmplatform.services.identity.entity.Role;
import com.eee.filmplatform.services.identity.entity.User;

public interface UserRoleService {

    void assignRole(User user);

    void assignRole(User user, Role role);

}