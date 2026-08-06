package com.eee.filmplatform.services.identity.service;

import com.eee.filmplatform.services.identity.entity.User;
import com.eee.filmplatform.services.identity.enums.AuditAction;
import com.eee.filmplatform.services.identity.enums.AuditStatus;

public interface AuditService {

    void log(
            AuditAction action,
            AuditStatus status,
            User user,
             String username,
            String details);
}
