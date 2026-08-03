package com.eee.filmplatform.services.identity.repository;

import com.eee.filmplatform.services.identity.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RolePermissionRepository extends JpaRepository<RolePermission, UUID> {
}