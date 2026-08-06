package com.eee.filmplatform.services.identity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI filmPlatformOpenAPI() {

        return new OpenAPI()

                .info(new Info()
                        .title("Film Production Platform - Identity Service API")
                        .description("""
                                Identity & Access Management APIs

                                Features
                                • Authentication
                                • Authorization
                                • JWT Authentication
                                • Refresh Tokens
                                • Email Verification
                                • Password Reset
                                • User Profile
                                • Audit Logging
                                """)
                        .version("v1")
                        .contact(new Contact()
                                .name("EEE Team")
                                .email("support@filmplatform.com"))
                        .license(new License()
                                .name("Proprietary")))

                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication"))

                .schemaRequirement(
                        "Bearer Authentication",

                        new SecurityScheme()

                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))

                .externalDocs(
                        new ExternalDocumentation()
                                .description("Film Production Platform"));
    }
}