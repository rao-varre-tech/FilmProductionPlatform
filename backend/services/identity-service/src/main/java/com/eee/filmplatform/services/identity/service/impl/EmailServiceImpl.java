package com.eee.filmplatform.services.identity.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.eee.filmplatform.services.identity.config.MailProperties;
import com.eee.filmplatform.services.identity.service.EmailService;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    private final MailProperties mailProperties;

    @Override
    public void sendTextEmail(
            String to,
            String subject,
            String body) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(mailProperties.username());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    @Override
    public void sendHtmlEmail(String to,
            String subject, String html) {

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(mailProperties.username());
            helper.setTo(to);
            helper.setSubject(subject);

            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception ex) {
            throw new RuntimeException("Failed to send email", ex);
        }
    }
}
