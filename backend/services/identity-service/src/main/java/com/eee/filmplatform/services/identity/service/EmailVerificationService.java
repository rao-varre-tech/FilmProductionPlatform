package com.eee.filmplatform.services.identity.service;

import com.eee.filmplatform.services.identity.entity.EmailVerificationToken;
import com.eee.filmplatform.services.identity.entity.User;

public interface EmailVerificationService {

    EmailVerificationToken create(User user);

    String verify(String token);
}
