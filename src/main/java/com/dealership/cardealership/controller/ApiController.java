package com.dealership.cardealership.controller;

import com.dealership.cardealership.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Controller for handling API requests.
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    private final VehicleRepository vehicleRepository;
    
    @Autowired
    public ApiController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }
    
    /**
     * Get all models for a given make.
     * 
     * @param make the vehicle make
     * @return a list of model names
     */
    @GetMapping("/models")
    public List<String> getModelsByMake(@RequestParam String make) {
        return vehicleRepository.findAllModelsByMake(make);
    }
} 