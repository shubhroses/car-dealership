package com.dealership.cardealership.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(MailConfig.class);
    
    @Value("${spring.mail.host:smtp.gmail.com}")
    private String host;
    
    @Value("${spring.mail.port:587}")
    private int port;
    
    @Value("${spring.mail.username:}")
    private String username;
    
    @Value("${spring.mail.password:}")
    private String password;
    
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        
        // Only set credentials if both username and password are provided
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            logger.info("Configuring email with username: {}", username);
            mailSender.setUsername(username);
            mailSender.setPassword(password);
            
            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            props.put("mail.smtp.timeout", "5000");
            props.put("mail.smtp.connectiontimeout", "5000");
            
            // Set debug to true to see detailed logs
            props.put("mail.debug", "true");
            
            logger.info("JavaMailSender configured with host: {}, port: {}", host, port);
            return mailSender;
        } else {
            logger.warn("Email credentials not configured properly. Email sending will fall back to logging only.");
            // Return null to trigger the fallback in EmailService
            return null;
        }
    }
} 