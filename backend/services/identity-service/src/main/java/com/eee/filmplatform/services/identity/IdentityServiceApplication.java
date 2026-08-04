package com.eee.filmplatform.services.identity;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.eee.filmplatform.services.identity.security.jwt.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class IdentityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdentityServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner verifyJwtProperties(JwtProperties jwtProperties) {
		return args -> {
			System.out.println("JWT Issuer: " + jwtProperties.getIssuer());
			System.out.println("Access Expiration: " + jwtProperties.getAccessTokenExpiration());
		};
	}

}
