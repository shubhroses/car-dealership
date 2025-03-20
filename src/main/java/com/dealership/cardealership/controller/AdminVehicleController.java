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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Controller
@RequestMapping("/admin/vehicles")
public class AdminVehicleController {

    @Autowired
    private VehicleRepository vehicleRepository;

    /**
     * Display list of all vehicles
     */
    @GetMapping
    public String listVehicles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Vehicle.VehicleStatus status,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minModelYear,
            @RequestParam(required = false) Integer maxModelYear,
            @RequestParam(required = false) String keyword,
            Model uiModel) {
        
        // Create pageable request with sorting
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        
        // Get vehicles with specified filters (basic implementation)
        Page<Vehicle> vehiclesPage;
        if (keyword != null && !keyword.isEmpty()) {
            // Search by keyword (VIN, make, model, etc.)
            vehiclesPage = vehicleRepository.findByKeyword(keyword, pageable);
        } else if (make != null && !make.isEmpty()) {
            // Filter by make and possibly other parameters
            vehiclesPage = vehicleRepository.findByMake(make, pageable);
        } else {
            // Get all vehicles
            vehiclesPage = vehicleRepository.findAll(pageable);
        }
        
        // Get all available makes for the filter dropdown
        List<String> makes = vehicleRepository.findAllMakes();
        
        // Add attributes to the model
        uiModel.addAttribute("vehicles", vehiclesPage.getContent());
        uiModel.addAttribute("makes", makes);
        uiModel.addAttribute("currentPage", page);
        uiModel.addAttribute("totalPages", vehiclesPage.getTotalPages());
        uiModel.addAttribute("size", size);
        
        // Add filter parameters to model for form re-population
        uiModel.addAttribute("selectedMake", make);
        uiModel.addAttribute("selectedModel", model);
        uiModel.addAttribute("selectedStatus", status);
        uiModel.addAttribute("minPrice", minPrice);
        uiModel.addAttribute("maxPrice", maxPrice);
        uiModel.addAttribute("minModelYear", minModelYear);
        uiModel.addAttribute("maxModelYear", maxModelYear);
        uiModel.addAttribute("keyword", keyword);
        
        return "admin/vehicles";
    }
    
    /**
     * Show form to add a new vehicle
     */
    @GetMapping("/new")
    public String newVehicleForm(Model model) {
        model.addAttribute("vehicle", new Vehicle());
        model.addAttribute("makes", vehicleRepository.findAllMakes());
        model.addAttribute("pageTitle", "Add New Vehicle");
        return "admin/vehicle-form";
    }
    
    /**
     * Show form to edit an existing vehicle
     */
    @GetMapping("/{id}/edit")
    public String editVehicleForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(id);
        
        if (vehicleOpt.isPresent()) {
            model.addAttribute("vehicle", vehicleOpt.get());
            model.addAttribute("makes", vehicleRepository.findAllMakes());
            model.addAttribute("pageTitle", "Edit Vehicle");
            return "admin/vehicle-form";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Vehicle not found with ID: " + id);
            return "redirect:/admin/vehicles";
        }
    }
    
    /**
     * Save a new or existing vehicle
     */
    @PostMapping("/save")
    public String saveVehicle(@Valid @ModelAttribute Vehicle vehicle, 
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "admin/vehicle-form";
        }
        
        try {
            // Save the vehicle
            vehicleRepository.save(vehicle);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                    vehicle.getId() == null ? "Vehicle added successfully!" : "Vehicle updated successfully!");
            
            // Redirect based on the form submit button clicked
            return "redirect:/admin/vehicles";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving vehicle: " + e.getMessage());
            return "redirect:/admin/vehicles";
        }
    }
    
    /**
     * View vehicle details
     */
    @GetMapping("/{id}")
    public String viewVehicle(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(id);
        
        if (vehicleOpt.isPresent()) {
            model.addAttribute("vehicle", vehicleOpt.get());
            return "admin/vehicle-detail";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Vehicle not found with ID: " + id);
            return "redirect:/admin/vehicles";
        }
    }
    
    /**
     * Delete a vehicle
     */
    @PostMapping("/{id}/delete")
    public String deleteVehicle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            vehicleRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Vehicle deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting vehicle: " + e.getMessage());
        }
        
        return "redirect:/admin/vehicles";
    }
    
    /**
     * Show form to manage vehicle images
     */
    @GetMapping("/{id}/images")
    public String manageImages(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(id);
        
        if (vehicleOpt.isPresent()) {
            model.addAttribute("vehicle", vehicleOpt.get());
            return "admin/vehicle-images";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Vehicle not found with ID: " + id);
            return "redirect:/admin/vehicles";
        }
    }
} 