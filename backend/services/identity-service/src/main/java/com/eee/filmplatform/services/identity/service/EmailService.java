package com.eee.filmplatform.services.identity.service;

public interface EmailService {

    void sendTextEmail(
            String to,
            String subject,
            String body);

    void sendHtmlEmail(
            String to,
            String subject,
            String html);

}
