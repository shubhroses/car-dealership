package com.dealership.cardealership.controller;

import com.dealership.cardealership.model.User;
import com.dealership.cardealership.model.User.UserRole;
import com.dealership.cardealership.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public String userManagement(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @PostMapping("/users/{userId}/add-admin-role")
    public String addAdminRole(@PathVariable Long userId, RedirectAttributes redirectAttributes) {
        Optional<User> userOptional = userRepository.findById(userId);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            if (!user.hasRole(UserRole.ROLE_ADMIN)) {
                user.addRole(UserRole.ROLE_ADMIN);
                userRepository.save(user);
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Admin role added successfully to " + user.getUsername());
            } else {
                redirectAttributes.addFlashAttribute("infoMessage", 
                    user.getUsername() + " already has Admin role");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found");
        }
        
        return "redirect:/admin/users";
    }
    
    @PostMapping("/users/{userId}/remove-admin-role")
    public String removeAdminRole(@PathVariable Long userId, RedirectAttributes redirectAttributes) {
        Optional<User> userOptional = userRepository.findById(userId);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            if (user.hasRole(UserRole.ROLE_ADMIN)) {
                user.removeRole(UserRole.ROLE_ADMIN);
                userRepository.save(user);
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Admin role removed successfully from " + user.getUsername());
            } else {
                redirectAttributes.addFlashAttribute("infoMessage", 
                    user.getUsername() + " does not have Admin role");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found");
        }
        
        return "redirect:/admin/users";
    }
} 