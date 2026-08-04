package com.eee.filmplatform.services.identity.repository;

import com.eee.filmplatform.services.identity.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import com.eee.filmplatform.services.identity.enums.PermissionName;

import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {

    Optional<Permission> findByName(PermissionName name);

    boolean existsByName(PermissionName name);
}