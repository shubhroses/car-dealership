package com.dealership.cardealership;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

/**
 * A standalone test class to verify Gmail connectivity.
 * This can be run independently to test if the Gmail account allows SMTP connections.
 */
public class TestEmailSender {

    // Replace these with your actual Gmail credentials
    private static final String username = "shubhroses@gmail.com";
    private static final String password = "bwmrnpnhsgqoehff";
    
    public static void main(String[] args) {
        // Setup mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.debug", "true");
        
        System.out.println("Setting up session with username: " + username);
        
        // Create a session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        
        try {
            System.out.println("Preparing to send test email...");
            
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(username));
            message.setSubject("Test Email from Car Dealership App");
            message.setText("This is a test email to verify the Gmail SMTP connection is working.");
            
            System.out.println("Sending message...");
            Transport.send(message);
            System.out.println("Message sent successfully!");
            
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 