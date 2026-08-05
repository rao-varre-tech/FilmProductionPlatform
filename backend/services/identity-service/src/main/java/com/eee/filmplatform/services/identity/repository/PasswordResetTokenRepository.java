package com.eee.filmplatform.services.identity.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.eee.filmplatform.services.identity.entity.PasswordResetToken;
import com.eee.filmplatform.services.identity.entity.User;

public interface PasswordResetTokenRepository 
                                extends JpaRepository<PasswordResetToken, UUID>{

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByUser(User user);

    List<PasswordResetToken> findByUserAndUsedFalse(User user);
    
}
