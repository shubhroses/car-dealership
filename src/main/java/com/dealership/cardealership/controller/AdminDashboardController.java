package com.dealership.cardealership.controller;

import com.dealership.cardealership.model.Vehicle;
import com.dealership.cardealership.model.Inquiry;
import com.dealership.cardealership.repository.VehicleRepository;
import com.dealership.cardealership.repository.InquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private InquiryRepository inquiryRepository;
    
    /**
     * Display admin dashboard with statistics and recent activity
     */
    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        // Get statistics
        long totalVehicles = vehicleRepository.count();
        long availableVehicles = vehicleRepository.countByStatus(Vehicle.VehicleStatus.AVAILABLE);
        long newInquiries = inquiryRepository.countByStatus(Inquiry.InquiryStatus.NEW);
        
        // Add statistics to model
        model.addAttribute("totalVehicles", totalVehicles);
        model.addAttribute("availableVehicles", availableVehicles);
        model.addAttribute("newInquiries", newInquiries);
        
        // Get recent activities (will be implemented with a service in the future)
        // For now, we're just displaying placeholder data in the template
        
        return "admin/dashboard";
    }
    
    /**
     * Redirect root admin URL to dashboard
     */
    @GetMapping("")
    public String redirectToDashboard() {
        return "redirect:/admin/dashboard";
    }
} 