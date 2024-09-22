package com.example.training.java.controllers;

import com.example.training.java.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final JwtUtils jwtUtils;

//    @ResponseBody
//    @GetMapping("/jwt/generate/token")
//    public String generateJwtToken(Authentication authentication) {
//        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
//        Collection<? extends GrantedAuthority> authorities = ((UserDetails) authentication.getPrincipal()).getAuthorities();
//        return jwtUtils.generateJwtToken(username, authorities);
//    }

//    @ResponseBody
//    @PostMapping("/jwt/refresh/token")
//    public String refreshToken(@RequestBody String refreshToken) {
//        jwtUtils.validateRequestToken(refreshToken);
//        String username = jwtUtils.getUserNameFromJwtToken(refreshToken);
//        Collection<? extends GrantedAuthority> authorities = jwtUtils.getClaimFromJwtToken(refreshToken);
//        return jwtUtils.generateJwtToken(username, authorities);
//    }
}
