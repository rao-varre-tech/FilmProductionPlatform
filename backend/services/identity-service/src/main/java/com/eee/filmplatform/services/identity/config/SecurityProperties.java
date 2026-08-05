package com.eee.filmplatform.services.identity.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.*;

@Configuration
@ConfigurationProperties(prefix = "security.login")
@Getter
@Setter
public class SecurityProperties {

    private int maxFailedAttempts;

    private long lockDurationMinutes;
}
