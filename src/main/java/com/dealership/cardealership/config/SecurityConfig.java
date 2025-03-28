package com.dealership.cardealership.config;

import com.dealership.cardealership.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security configuration for the application.
 * This configures Spring Security for development purposes.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Public pages
                .requestMatchers("/", "/vehicles", "/vehicles/*", 
                             "/about", "/contact", "/register", 
                             "/css/**", "/js/**", "/images/**", 
                             "/api/**", "/h2-console/**").permitAll()
                // Admin area
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Customer area
                .requestMatchers("/customer/**").hasRole("CUSTOMER")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )
            .rememberMe(remember -> remember
                .key("uniqueAndSecretKey")
                .tokenValiditySeconds(7 * 24 * 60 * 60) // 1 week
                .userDetailsService(userDetailsService)
            )
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/access-denied")
            );
        
        // Disable CSRF for H2 console
        http.csrf(csrf -> csrf
            .ignoringRequestMatchers("/h2-console/**")
        );
        
        // Allow H2 console to be displayed in iframe
        http.headers(headers -> headers
            .frameOptions(frame -> frame
                .sameOrigin()
                .disable()
            )
        );
        
        return http.build();
    }
} 