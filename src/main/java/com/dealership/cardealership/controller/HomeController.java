package com.dealership.cardealership.controller;

import com.dealership.cardealership.model.Vehicle;
import com.dealership.cardealership.repository.VehicleRepository;
import com.dealership.cardealership.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for the home page and public vehicle browsing.
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private EmailService emailService;
    
    /**
     * Display the home page with featured vehicles
     */
    @GetMapping("/")
    public String home(Model model) {
        // Get featured vehicles (newest ones)
        List<Vehicle> featuredVehicles = vehicleRepository.findByStatus(
                Vehicle.VehicleStatus.AVAILABLE, 
                PageRequest.of(0, 6, Sort.by("id").descending())
        ).getContent();
        
        // Get all makes for the search form
        List<String> makes = vehicleRepository.findAllMakes();
        
        model.addAttribute("featuredVehicles", featuredVehicles);
        model.addAttribute("makes", makes);
        
        return "home";
    }
    
    /**
     * Display the about page
     */
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    
    /**
     * Display the contact page
     */
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
    
    /**
     * Handle contact form submission
     */
    @PostMapping("/contact/submit")
    public String submitContactForm(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam(required = false) String phone,
            @RequestParam String subject,
            @RequestParam String message,
            RedirectAttributes redirectAttributes) {
        
        // Log the contact submission
        logger.info("Contact form submission from: {} ({})", name, email);
        logger.info("Subject: {}", subject);
        
        try {
            // Build the email message
            String emailBody = String.format(
                "Contact Form Submission:\n\n" +
                "Name: %s\n" +
                "Email: %s\n" +
                "Phone: %s\n\n" +
                "Message:\n%s",
                name, email, phone != null ? phone : "Not provided", message
            );
            
            // Send to dealership contact email
            emailService.sendEmail(
                emailService.getContactEmail(),   // To: dealership
                "Contact Form: " + subject,       // Subject
                emailBody                         // Body
            );
            
            // Send confirmation to the customer
            String confirmationBody = String.format(
                "Dear %s,\n\n" +
                "Thank you for contacting Premium Auto Dealership. " +
                "We have received your message regarding \"%s\" and will get back to you shortly.\n\n" +
                "Your message:\n%s\n\n" +
                "Best regards,\n" +
                "Premium Auto Dealership Team",
                name, subject, message
            );
            
            emailService.sendEmail(
                email,                           // To: customer
                "Thank you for contacting Premium Auto Dealership", // Subject
                confirmationBody                 // Body
            );
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Thank you for your message, " + name + "! We'll get back to you soon.");
        } catch (Exception e) {
            logger.error("Error sending email: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Sorry, there was a problem sending your message. Please try again or contact us directly.");
        }
        
        return "redirect:/contact";
    }
    
    /**
     * Display vehicle list with filtering and pagination
     */
    @GetMapping("/vehicles")
    public String listVehicles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Integer minModelYear,
            @RequestParam(required = false) Integer maxModelYear,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Model uiModel) {
        
        // Create pageable request with sorting (newest first)
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        
        // Get vehicles with filters (basic implementation)
        Page<Vehicle> vehiclesPage;
        if (make != null && !make.isEmpty()) {
            vehiclesPage = vehicleRepository.findByMakeAndStatusOrderByIdDesc(
                    make, Vehicle.VehicleStatus.AVAILABLE, pageable);
        } else {
            vehiclesPage = vehicleRepository.findByStatus(
                    Vehicle.VehicleStatus.AVAILABLE, pageable);
        }
        
        // Get all makes for the filter dropdown
        List<String> makes = vehicleRepository.findAllMakes();
        
        // If make is selected, get models for that make
        List<String> models = null;
        if (make != null && !make.isEmpty()) {
            models = vehicleRepository.findAllModelsByMake(make);
        }
        
        // Add attributes to the model
        uiModel.addAttribute("vehicles", vehiclesPage.getContent());
        uiModel.addAttribute("makes", makes);
        uiModel.addAttribute("models", models);
        uiModel.addAttribute("currentPage", page);
        uiModel.addAttribute("totalPages", vehiclesPage.getTotalPages());
        uiModel.addAttribute("size", size);
        
        // Add filter parameters to model for form re-population
        uiModel.addAttribute("selectedMake", make);
        uiModel.addAttribute("selectedModel", model);
        uiModel.addAttribute("minModelYear", minModelYear);
        uiModel.addAttribute("maxModelYear", maxModelYear);
        uiModel.addAttribute("minPrice", minPrice);
        uiModel.addAttribute("maxPrice", maxPrice);
        
        return "vehicles/list";
    }
    
    /**
     * Display vehicle details
     */
    @GetMapping("/vehicles/{id}")
    public String viewVehicle(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(id);
        
        if (vehicleOpt.isPresent()) {
            model.addAttribute("vehicle", vehicleOpt.get());
            return "vehicles/details";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Vehicle not found.");
            return "redirect:/vehicles";
        }
    }
} 