package com.eee.filmplatform.services.identity.service.impl;

import org.springframework.stereotype.Service;

import com.eee.filmplatform.services.identity.common.RequestContext;
import com.eee.filmplatform.services.identity.entity.AuditLog;
import com.eee.filmplatform.services.identity.entity.User;
import com.eee.filmplatform.services.identity.enums.AuditAction;
import com.eee.filmplatform.services.identity.enums.AuditStatus;
import com.eee.filmplatform.services.identity.repository.AuditLogRepository;
import com.eee.filmplatform.services.identity.service.AuditService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    private final RequestContext requestContext;

    @Override
    @Transactional
    public void log(
            AuditAction action,
            AuditStatus status,
            User user,
            String username,
            String details) {

        AuditLog auditLog = AuditLog.builder()
                .action(action)
                .status(status)
                .user(user)
                .username(username)
                .ipAddress(requestContext.getIpAddress())
                .userAgent(requestContext.getUserAgent())
                .details(details)
                .build();

        auditLogRepository.save(auditLog);
    }
}
