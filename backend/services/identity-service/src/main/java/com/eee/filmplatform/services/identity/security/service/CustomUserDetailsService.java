package com.eee.filmplatform.services.identity.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eee.filmplatform.services.identity.entity.User;
import com.eee.filmplatform.services.identity.repository.UserRepository;
import com.eee.filmplatform.services.identity.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository
                .findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        CustomUserDetails userDetails = new CustomUserDetails(user);

        // DEBUG
        System.out.println("====================================");
        System.out.println("User      : " + userDetails.getUsername());
        System.out.println("Authorities: " + userDetails.getAuthorities());
        System.out.println("====================================");

        return userDetails;
    }
}