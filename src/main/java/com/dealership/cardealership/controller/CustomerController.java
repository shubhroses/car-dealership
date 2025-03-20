package com.dealership.cardealership.controller;

import com.dealership.cardealership.model.Inquiry;
import com.dealership.cardealership.model.User;
import com.dealership.cardealership.model.Vehicle;
import com.dealership.cardealership.repository.InquiryRepository;
import com.dealership.cardealership.repository.UserRepository;
import com.dealership.cardealership.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controller for customer-specific operations.
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private InquiryRepository inquiryRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    /**
     * Display customer dashboard with inquiries and profile information
     */
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            // Get recent inquiries for this user
            List<Inquiry> inquiries = inquiryRepository.findByUserOrderByInquiryDateDesc(user);
            
            // Add to model
            model.addAttribute("user", user);
            model.addAttribute("inquiries", inquiries);
            
            return "customer/dashboard";
        } else {
            return "redirect:/login";
        }
    }
    
    /**
     * Display list of all inquiries for the current customer
     */
    @GetMapping("/inquiries")
    public String listInquiries(Authentication authentication, Model model) {
        String username = authentication.getName();
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            // Get all inquiries for this user
            List<Inquiry> inquiries = inquiryRepository.findByUserOrderByInquiryDateDesc(user);
            
            // Add to model
            model.addAttribute("inquiries", inquiries);
            
            return "customer/inquiries";
        } else {
            return "redirect:/login";
        }
    }
    
    /**
     * Display details of a specific inquiry
     */
    @GetMapping("/inquiries/{id}")
    public String viewInquiry(@PathVariable Long id, Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Optional<Inquiry> inquiryOpt = inquiryRepository.findById(id);
            
            if (inquiryOpt.isPresent()) {
                Inquiry inquiry = inquiryOpt.get();
                
                // Ensure the inquiry belongs to the current user
                if (inquiry.getUser().getId().equals(user.getId())) {
                    model.addAttribute("inquiry", inquiry);
                    return "customer/inquiry-detail";
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "You don't have permission to view this inquiry.");
                    return "redirect:/customer/inquiries";
                }
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Inquiry not found.");
                return "redirect:/customer/inquiries";
            }
        } else {
            return "redirect:/login";
        }
    }
    
    /**
     * Submit a new inquiry for a vehicle
     */
    @PostMapping("/inquiries/vehicle/{vehicleId}")
    public String submitInquiry(
            @PathVariable Long vehicleId,
            @RequestParam String message,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        
        String username = authentication.getName();
        Optional<User> userOpt = userRepository.findByUsername(username);
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);
        
        if (userOpt.isPresent() && vehicleOpt.isPresent()) {
            User user = userOpt.get();
            Vehicle vehicle = vehicleOpt.get();
            
            // Create and save inquiry
            Inquiry inquiry = new Inquiry();
            inquiry.setUser(user);
            inquiry.setVehicle(vehicle);
            inquiry.setMessage(message);
            inquiry.setInquiryDate(LocalDateTime.now());
            inquiry.setStatus(Inquiry.InquiryStatus.NEW);
            
            inquiryRepository.save(inquiry);
            
            redirectAttributes.addFlashAttribute("successMessage", "Your inquiry has been submitted successfully. We will respond shortly.");
            return "redirect:/vehicles/" + vehicleId;
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error submitting inquiry. Please try again.");
            return "redirect:/vehicles/" + vehicleId;
        }
    }
} 