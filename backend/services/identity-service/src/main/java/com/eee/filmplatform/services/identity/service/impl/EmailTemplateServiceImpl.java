package com.eee.filmplatform.services.identity.service.impl;

import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import com.eee.filmplatform.services.identity.service.EmailTemplateService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private final SpringTemplateEngine templateEngine;

    @Override
    public String verificationEmail(
            String firstName,
            String verificationUrl) {

        Context context = new Context();

        context.setVariable("firstName", firstName);
        context.setVariable("verificationUrl", verificationUrl);

        return templateEngine.process(
                "verification-email",
                context);
    }

   @Override
    public String passwordResetEmail(
            String firstName,
            String resetUrl) {

        Context context = new Context();

        context.setVariable("firstName", firstName);
        context.setVariable("resetUrl", resetUrl);

        return templateEngine.process(
                "password-reset-email",
                context);
    }
    
}
