package com.dealership.cardealership.controller;

import com.dealership.cardealership.model.Address;
import com.dealership.cardealership.model.User;
import com.dealership.cardealership.model.User.UserRole;
import com.dealership.cardealership.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.time.LocalDateTime;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    /**
     * Display the login page
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    /**
     * Display the registration page
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        User user = new User();
        Address address = new Address();
        
        model.addAttribute("user", user);
        model.addAttribute("address", address);
        
        return "register";
    }
    
    /**
     * Process the registration form submission
     */
    @PostMapping("/register")
    public String processRegistration(
            @Valid @ModelAttribute("user") User user,
            BindingResult userResult,
            @Valid @ModelAttribute("address") Address address,
            BindingResult addressResult,
            RedirectAttributes redirectAttributes) {
        
        // Check for validation errors
        if (userResult.hasErrors() || addressResult.hasErrors()) {
            return "register";
        }
        
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            userResult.rejectValue("username", "error.user", "Username is already taken");
            return "register";
        }
        
        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            userResult.rejectValue("email", "error.user", "Email is already registered");
            return "register";
        }
        
        // Prepare user for registration
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addRole(User.UserRole.ROLE_CUSTOMER); // Default role for registration
        user.setActive(true);
        
        // Set address to user
        user.setAddress(address);
        
        // Save the user
        userRepository.save(user);
        
        // Add success message and redirect to login page
        redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please login with your credentials.");
        return "redirect:/login";
    }
    
    /**
     * Display the access denied page
     */
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
} 