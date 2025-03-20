package com.dealership.cardealership.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;
    
    @Autowired
    private Environment environment;
    
    @Value("${spring.mail.username:noreply@premiumauto.com}")
    private String fromEmail;
    
    @Value("${app.contact.email:info@premiumauto.com}")
    private String contactEmail;
    
    @Value("${app.development.override.email:#{null}}")
    private String developmentOverrideEmail;
    
    /**
     * Send an email
     * 
     * @param to recipient email
     * @param subject email subject
     * @param body email body
     */
    public void sendEmail(String to, String subject, String body) {
        sendEmail(to, fromEmail, subject, body);
    }
    
    /**
     * Send an email with specified from address
     * 
     * @param to recipient email
     * @param from sender email
     * @param subject email subject
     * @param body email body
     */
    public void sendEmail(String to, String from, String subject, String body) {
        try {
            // Override recipient email in development mode if configured
            String actualRecipient = to;
            if (isDevelopmentMode() && developmentOverrideEmail != null && !developmentOverrideEmail.isEmpty()) {
                logger.info("Development mode: Redirecting email from {} to {}", to, developmentOverrideEmail);
                actualRecipient = developmentOverrideEmail;
                subject = "[ORIGINALLY TO: " + to + "] " + subject;
            }
            
            logger.debug("Email Settings - Active profiles: {}", Arrays.toString(environment.getActiveProfiles()));
            logger.debug("Email Settings - From: {}", from);
            logger.debug("Email Settings - To: {}", actualRecipient);
            
            if (mailSender != null) {
                logger.debug("Mail sender is available. Attempting to send email...");
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(from);
                message.setTo(actualRecipient);
                message.setSubject(subject);
                message.setText(body);
                
                try {
                    mailSender.send(message);
                    logger.info("Email sent successfully to: {}", actualRecipient);
                } catch (Exception e) {
                    // Log the error but don't crash - fall back to logging the message
                    logger.error("Failed to send email: {} ({})", e.getMessage(), e.getClass().getName());
                    e.printStackTrace();
                    logFallbackEmail(from, actualRecipient, subject, body);
                    throw e; // Re-throw so the controller can handle it
                }
            } else {
                // Fallback when mail sender is not configured - log the message
                logger.warn("Mail sender is NULL. Email cannot be sent.");
                logFallbackEmail(from, actualRecipient, subject, body);
            }
        } catch (Exception e) {
            // General exception handling
            logger.error("Error in email service: {} ({})", e.getMessage(), e.getClass().getName());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Check if the application is running in development mode
     */
    private boolean isDevelopmentMode() {
        return Arrays.asList(environment.getActiveProfiles()).contains("dev") || 
               (environment.getActiveProfiles().length == 0 && !Arrays.asList(environment.getActiveProfiles()).contains("prod"));
    }
    
    /**
     * Log the email details when real sending is not possible
     */
    private void logFallbackEmail(String from, String to, String subject, String body) {
        logger.info("EMAIL NOT SENT - Mail service not fully configured. Would have sent email:");
        logger.info("From: {}", from);
        logger.info("To: {}", to);
        logger.info("Subject: {}", subject);
        logger.info("Body: \n{}", body);
        
        // Also log a development hint
        if (to.equals(contactEmail) || to.equals(developmentOverrideEmail)) {
            logger.info("HINT: To enable actual email sending, configure spring.mail.username and spring.mail.password in application.properties");
        }
    }
    
    /**
     * Get the contact email address for the dealership
     */
    public String getContactEmail() {
        return contactEmail;
    }
} 