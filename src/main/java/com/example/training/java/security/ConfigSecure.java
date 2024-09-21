package com.example.training.java.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


/**
 * The prePostEnabled property enables Spring Security pre/post annotations.
 * The securedEnabled property determines if the @Secured annotation should be enabled.
 * The jsr250Enabled property allows us to use the @RoleAllowed annotation.
 */
@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(jsr250Enabled = true, prePostEnabled = false, securedEnabled = true)
public class ConfigSecure {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/customer/**").permitAll()
                        .requestMatchers("/file/**").permitAll()
                        .requestMatchers(IGNORE_SWAGGER).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    private static final String[] IGNORE_SWAGGER = {
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };
}
