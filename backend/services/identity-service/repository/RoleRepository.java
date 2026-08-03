package com.eee.filmplatform.services.identity.repository;

import com.eee.filmplatform.services.identity.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import com.eee.filmplatform.services.identity.enums.RoleName;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(RoleName name);

    boolean existsByName(RoleName name);
}