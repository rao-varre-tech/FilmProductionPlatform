package com.eee.filmplatform.services.identity.service;

import com.eee.filmplatform.services.identity.entity.RefreshToken;
import com.eee.filmplatform.services.identity.entity.User;

public interface RefreshTokenService {

    RefreshToken create(User user, String token);

    RefreshToken verify(String token);

    void revoke(String token);

    void revokeAll(User user);

}