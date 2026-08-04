package com.eee.filmplatform.services.identity.repository;

import com.eee.filmplatform.services.identity.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
}