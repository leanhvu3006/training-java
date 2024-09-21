package com.example.training.java.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.List;


/**
 * The prePostEnabled property enables Spring Security pre/post annotations.
 * The securedEnabled property determines if the @Secured annotation should be enabled.
 * The jsr250Enabled property allows us to use the @RoleAllowed annotation.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
@EnableMethodSecurity
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    private final JwtFilter jwtFilter;
    private final DataSource dataSource;
    private final UserDetailServiceImpl userDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/customer/**").permitAll()
                        .requestMatchers("/file/**").permitAll()
                        .requestMatchers(IGNORE_SWAGGER).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/index")
                        .loginProcessingUrl("/perform_login")
                        .successHandler(authenticationSuccessHandler())
//                        .failureHandler()
                        .permitAll())
                .authenticationProvider(authenticationProvider().getFirst())// apply 1 authenticationProvider
//                .authenticationManager(providerManager()) // apply to many authenticationProvider
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
                    httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(authenticationEntryPoint);
                })
                .rememberMe(rememberMe -> rememberMe
                        .rememberMeServices(tokenBasedRememberMeServices())
                )
//                .sessionManagement(sessionManager -> sessionManager
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////                        .invalidSessionUrl("/session-invalid")
//                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
//                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/index")
//                        .successHandler()
//                        .failureHandler()
                        .permitAll()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    // Remember-me tạo cookie pass authen, khi access nếu k SecurityContextHodel không có authentication thì nó sẽ vào RememberMeAUthenticationFilter để autoLogin.
    // Có 2 hình thức, 1 là lưu trong memory, 2 là PersistentTokenBasedRememberMeServices lưu vào DB.
    @Bean
    public PersistentTokenBasedRememberMeServices tokenBasedRememberMeServices() {
        return new PersistentTokenBasedRememberMeServices("secret", userDetailService, persistentTokenRepository());
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    public List<AuthenticationProvider> authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService);
//        authenticationProvider.setPasswordEncoder(passwordEncode());
        return List.of(authenticationProvider);
    }

    @Bean
    public ProviderManager providerManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncode() {
//    return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setAlwaysUseDefaultTargetUrl(false);
        successHandler.setDefaultTargetUrl("/manager");
        return successHandler;
    }

    private static final String[] IGNORE_SWAGGER = {
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };
}
