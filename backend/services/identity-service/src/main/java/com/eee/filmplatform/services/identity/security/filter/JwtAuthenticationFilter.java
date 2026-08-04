package com.eee.filmplatform.services.identity.security.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.eee.filmplatform.services.identity.security.CustomUserDetails;
import com.eee.filmplatform.services.identity.security.jwt.JwtService;
import com.eee.filmplatform.services.identity.security.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

       // Read Authorization header
        String authHeader = request.getHeader("Authorization");

        // Skip if no Bearer token is present
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Remove "Bearer "
        String jwt = authHeader.substring(7);

        // Extract username from JWT
        String username = jwtService.extractUsername(jwt);

        // Authenticate only if not already authenticated
        if (username != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load user from database
            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            // Validate JWT
            if (jwtService.isTokenValid(jwt, (CustomUserDetails) userDetails)) {

                // Create Authentication object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                // Attach request details
                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                // Store authenticated user in SecurityContext
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            }
        }

        // Continue request
        filterChain.doFilter(request, response);

    }
}