package com.dealership.cardealership.controller;

import com.dealership.cardealership.model.Inquiry;
import com.dealership.cardealership.model.Inquiry.InquiryStatus;
import com.dealership.cardealership.model.User;
import com.dealership.cardealership.repository.InquiryRepository;
import com.dealership.cardealership.repository.UserRepository;
import com.dealership.cardealership.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/inquiries")
public class AdminInquiryController {

    @Autowired
    private InquiryRepository inquiryRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private UserRepository userRepository;

    /**
     * Display list of all inquiries with filter options
     */
    @GetMapping
    public String listInquiries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) InquiryStatus status,
            @RequestParam(required = false) String vehicleMake,
            @RequestParam(required = false) String dateRange,
            @RequestParam(required = false) String keyword,
            Model model) {
        
        // Create pageable request with sorting (newest first)
        Pageable pageable = PageRequest.of(page, size, Sort.by("inquiryDate").descending());
        
        // Get inquiries with filters (basic implementation)
        Page<Inquiry> inquiriesPage;
        if (status != null) {
            inquiriesPage = inquiryRepository.findByStatus(status, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            inquiriesPage = inquiryRepository.findByKeyword(keyword, pageable);
        } else {
            inquiriesPage = inquiryRepository.findAll(pageable);
        }
        
        // Get all vehicle makes for filter dropdown
        List<String> makes = vehicleRepository.findAllMakes();
        
        // Add attributes to the model
        model.addAttribute("inquiries", inquiriesPage.getContent());
        model.addAttribute("makes", makes);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", inquiriesPage.getTotalPages());
        model.addAttribute("size", size);
        
        // Add filter parameters to model for form re-population
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedMake", vehicleMake);
        model.addAttribute("selectedDateRange", dateRange);
        model.addAttribute("keyword", keyword);
        
        return "admin/inquiries";
    }
    
    /**
     * Display inquiry details and response form
     */
    @GetMapping("/{id}")
    public String viewInquiry(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Inquiry> inquiryOpt = inquiryRepository.findById(id);
        
        if (inquiryOpt.isPresent()) {
            model.addAttribute("inquiry", inquiryOpt.get());
            return "admin/inquiry-detail";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Inquiry not found with ID: " + id);
            return "redirect:/admin/inquiries";
        }
    }
    
    /**
     * Process inquiry response
     */
    @PostMapping("/{id}/respond")
    public String respondToInquiry(
            @PathVariable Long id,
            @RequestParam String response,
            @RequestParam String action,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        
        Optional<Inquiry> inquiryOpt = inquiryRepository.findById(id);
        Optional<User> staffOpt = userRepository.findByUsername(authentication.getName());
        
        if (inquiryOpt.isPresent() && staffOpt.isPresent()) {
            Inquiry inquiry = inquiryOpt.get();
            User staff = staffOpt.get();
            
            inquiry.setResponse(response);
            inquiry.setResponseDate(LocalDateTime.now());
            inquiry.setStaff(staff);
            
            // Set status based on action
            switch (action) {
                case "respond":
                    inquiry.setStatus(InquiryStatus.RESPONDED);
                    redirectAttributes.addFlashAttribute("successMessage", "Response sent successfully!");
                    break;
                case "in_progress":
                    inquiry.setStatus(InquiryStatus.IN_PROGRESS);
                    redirectAttributes.addFlashAttribute("successMessage", "Inquiry marked as in progress.");
                    break;
                case "save":
                    // Keep current status
                    redirectAttributes.addFlashAttribute("successMessage", "Response saved as draft.");
                    break;
                default:
                    break;
            }
            
            inquiryRepository.save(inquiry);
            return "redirect:/admin/inquiries/" + id;
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Inquiry not found with ID: " + id);
            return "redirect:/admin/inquiries";
        }
    }
    
    /**
     * Update an existing response
     */
    @PostMapping("/{id}/update")
    public String updateResponse(
            @PathVariable Long id,
            @RequestParam String response,
            @RequestParam String action,
            RedirectAttributes redirectAttributes) {
        
        Optional<Inquiry> inquiryOpt = inquiryRepository.findById(id);
        
        if (inquiryOpt.isPresent()) {
            Inquiry inquiry = inquiryOpt.get();
            
            inquiry.setResponse(response);
            inquiry.setResponseDate(LocalDateTime.now());
            
            // Set status based on action
            if ("close".equals(action)) {
                inquiry.setStatus(InquiryStatus.CLOSED);
                redirectAttributes.addFlashAttribute("successMessage", "Inquiry closed successfully.");
            } else {
                redirectAttributes.addFlashAttribute("successMessage", "Response updated successfully.");
            }
            
            inquiryRepository.save(inquiry);
            return "redirect:/admin/inquiries/" + id;
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Inquiry not found with ID: " + id);
            return "redirect:/admin/inquiries";
        }
    }
} 