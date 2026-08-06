package com.eee.filmplatform.services.identity.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eee.filmplatform.services.identity.entity.AuditLog;

@Repository
public interface AuditLogRepository
        extends JpaRepository<AuditLog, UUID> {
}
