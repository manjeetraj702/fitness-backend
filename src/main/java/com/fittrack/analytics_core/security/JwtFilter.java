package com.fittrack.analytics_core.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Get the Authorization Header from the request
        final String authHeader = request.getHeader("Authorization");

        // 2. Check if it exists and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; // No token found, move on (SecurityConfig will block it later if needed)
        }

        // 3. Extract the token (Remove "Bearer " prefix)
        final String token = authHeader.substring(7);

        // 4. Validate the token and set the security context
        if (jwtUtil.isTokenValid(token)) {
            String email = jwtUtil.extractEmail(token);
            
            // Tell Spring Security: "This user is valid and logged in!"
            UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // 5. Continue the request
        filterChain.doFilter(request, response);
    }
}