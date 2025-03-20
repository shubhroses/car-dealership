package com.dealership.cardealership.controller;

import com.dealership.cardealership.model.Vehicle;
import com.dealership.cardealership.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * Controller for the home page and public vehicle browsing.
 */
@Controller
public class HomeController {

    @Autowired
    private VehicleRepository vehicleRepository;
    
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