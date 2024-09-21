package com.example.training.java.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${jwt.expire.time}")
    private Integer jwtTokenExpire;

    @Value("${jwt.secret.key}")
    private String secretKey;

    public String generateJwtToken(String username, Collection<? extends GrantedAuthority> authorities) {
        return Jwts.builder()
                .setSubject(username)
                .claim("authorization", authorities)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtTokenExpire))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public boolean validateRequestToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            return true;
        } catch (Exception e) {
            //TODO lots of exception can detach and log correct error happend
            log.error("validate request jwt token invalid");
            throw e;
        }
    }

    public String getUserNameFromJwtToken(String token) {
        var a = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("authorization");
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public Collection<? extends GrantedAuthority> getClaimFromJwtToken(String token) {
        return (Collection<? extends GrantedAuthority>) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("authorization");
    }
}
