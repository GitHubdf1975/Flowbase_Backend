package com.example.flowbase_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Configuration for handling security exceptions
 */
@Configuration
public class SecurityExceptionConfig {

    /**
     * Custom AccessDeniedHandler to handle AccessDeniedException
     * @return AccessDeniedHandler
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
        accessDeniedHandler.setErrorPage("/error");
        return accessDeniedHandler;
    }

    /**
     * Custom AuthenticationEntryPoint to handle authentication failures
     * @return AuthenticationEntryPoint
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        // Return 401 Unauthorized instead of redirecting to a login page
        return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
    }
}