package com.eee.filmplatform.services.identity.service;

public interface EmailTemplateService {

    String verificationEmail(
            String firstName,
            String verificationUrl);

    String passwordResetEmail(
            String firstName,
            String resetUrl);
}
