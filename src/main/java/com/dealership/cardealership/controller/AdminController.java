package com.dealership.cardealership.controller;

import com.dealership.cardealership.model.Inquiry;
import com.dealership.cardealership.model.Vehicle;
import com.dealership.cardealership.repository.InquiryRepository;
import com.dealership.cardealership.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import java.util.List;

/**
 * Controller for admin/dealership operations.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final VehicleRepository vehicleRepository;
    private final InquiryRepository inquiryRepository;
    
    @Autowired
    public AdminController(VehicleRepository vehicleRepository, InquiryRepository inquiryRepository) {
        this.vehicleRepository = vehicleRepository;
        this.inquiryRepository = inquiryRepository;
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long availableVehicles = vehicleRepository.findByStatus(Vehicle.VehicleStatus.AVAILABLE).size();
        long totalVehicles = vehicleRepository.count();
        long newInquiries = inquiryRepository.findByStatus(Inquiry.InquiryStatus.NEW).size();
        
        model.addAttribute("availableVehicles", availableVehicles);
        model.addAttribute("totalVehicles", totalVehicles);
        model.addAttribute("newInquiries", newInquiries);
        
        return "admin/dashboard";
    }
    
    /* Commenting out vehicle-related methods - these are now handled by AdminVehicleController
    @GetMapping("/vehicles")
    public String listVehicles(Model model) {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        model.addAttribute("vehicles", vehicles);
        return "admin/vehicles/list";
    }
    
    @GetMapping("/vehicles/new")
    public String newVehicleForm(Model model) {
        model.addAttribute("vehicle", new Vehicle());
        model.addAttribute("fuelTypes", Vehicle.FuelType.values());
        model.addAttribute("transmissionTypes", Vehicle.TransmissionType.values());
        model.addAttribute("statusTypes", Vehicle.VehicleStatus.values());
        return "admin/vehicles/form";
    }
    
    @PostMapping("/vehicles")
    public String createVehicle(@Valid @ModelAttribute Vehicle vehicle, 
                              BindingResult result, 
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/vehicles/form";
        }
        
        vehicleRepository.save(vehicle);
        redirectAttributes.addFlashAttribute("successMessage", "Vehicle successfully added!");
        return "redirect:/admin/vehicles";
    }
    
    @GetMapping("/vehicles/{id}/edit")
    public String editVehicleForm(@PathVariable Long id, Model model) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("fuelTypes", Vehicle.FuelType.values());
        model.addAttribute("transmissionTypes", Vehicle.TransmissionType.values());
        model.addAttribute("statusTypes", Vehicle.VehicleStatus.values());
        return "admin/vehicles/form";
    }
    
    @PostMapping("/vehicles/{id}")
    public String updateVehicle(@PathVariable Long id, 
                              @Valid @ModelAttribute Vehicle vehicle, 
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/vehicles/form";
        }
        
        vehicle.setId(id);
        vehicleRepository.save(vehicle);
        redirectAttributes.addFlashAttribute("successMessage", "Vehicle successfully updated!");
        return "redirect:/admin/vehicles";
    }
    */
    
    /* Commenting out inquiry-related methods - these are now handled by AdminInquiryController
    @GetMapping("/inquiries")
    public String listInquiries(Model model) {
        List<Inquiry> inquiries = inquiryRepository.findAll();
        model.addAttribute("inquiries", inquiries);
        return "admin/inquiries/list";
    }
    
    @GetMapping("/inquiries/{id}")
    public String viewInquiry(@PathVariable Long id, Model model) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));
        
        model.addAttribute("inquiry", inquiry);
        model.addAttribute("statusTypes", Inquiry.InquiryStatus.values());
        return "admin/inquiries/details";
    }
    
    @PostMapping("/inquiries/{id}/respond")
    public String respondToInquiry(@PathVariable Long id, 
                                 @RequestParam String response, 
                                 @RequestParam Inquiry.InquiryStatus status,
                                 RedirectAttributes redirectAttributes) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));
        
        inquiry.setResponse(response);
        inquiry.setStatus(status);
        inquiryRepository.save(inquiry);
        
        redirectAttributes.addFlashAttribute("successMessage", "Response sent successfully!");
        return "redirect:/admin/inquiries";
    }
    */
} 