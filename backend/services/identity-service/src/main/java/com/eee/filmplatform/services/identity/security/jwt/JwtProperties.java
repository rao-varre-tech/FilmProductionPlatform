package com.eee.filmplatform.services.identity.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * Base64 encoded secret key
     */
    private String secret;

    /**
     * Access token validity in milliseconds
     */
    private long accessTokenExpiration;

    /**
     * Refresh token validity in milliseconds
     */
    private long refreshTokenExpiration;

    /**
     * Token issuer
     */
    private String issuer;
}