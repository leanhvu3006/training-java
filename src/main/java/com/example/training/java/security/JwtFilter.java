package com.example.training.java.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal (
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            final String header = request.getHeader(AUTHORIZATION);
            final String token = getTokenFromHeader(header);
            if(StringUtils.hasText(token) && jwtUtils.validateRequestToken(token)) {
                final String userNameFromJwtToken = jwtUtils.getUserNameFromJwtToken(token);
                final Collection<? extends GrantedAuthority> claimFromJwtToken = jwtUtils.getClaimFromJwtToken(token);
                final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userNameFromJwtToken, null, claimFromJwtToken);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            log.error("Can not set user to authentication: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromHeader(String header) {
        if(StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
