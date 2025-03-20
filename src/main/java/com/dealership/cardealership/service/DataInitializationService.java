package com.dealership.cardealership.service;

import com.dealership.cardealership.model.Address;
import com.dealership.cardealership.model.User;
import com.dealership.cardealership.model.Vehicle;
import com.dealership.cardealership.repository.UserRepository;
import com.dealership.cardealership.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Service to initialize the database with sample data on application startup.
 */
@Service
public class DataInitializationService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public DataInitializationService(UserRepository userRepository, 
                                    VehicleRepository vehicleRepository,
                                    PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initializeData() {
        // Only initialize if the database is empty
        if (userRepository.count() == 0 && vehicleRepository.count() == 0) {
            createUsers();
            createVehicles();
        }
    }
    
    private void createUsers() {
        // Create admin user
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setEmail("admin@cardealership.com");
        admin.setPhone("(555) 123-4567");
        admin.setActive(true);
        
        Address adminAddress = new Address();
        adminAddress.setStreet("123 Admin St");
        adminAddress.setCity("Admin City");
        adminAddress.setState("CA");
        adminAddress.setZipCode("12345");
        adminAddress.setCountry("USA");
        admin.setAddress(adminAddress);
        
        admin.setRoles(new HashSet<>());
        admin.addRole(User.UserRole.ROLE_ADMIN);
        admin.addRole(User.UserRole.ROLE_STAFF);
        
        // Create customer user
        User customer = new User();
        customer.setUsername("customer");
        customer.setPassword(passwordEncoder.encode("customer"));
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPhone("(555) 987-6543");
        customer.setActive(true);
        
        Address customerAddress = new Address();
        customerAddress.setStreet("456 Customer Ave");
        customerAddress.setCity("Customer City");
        customerAddress.setState("NY");
        customerAddress.setZipCode("67890");
        customerAddress.setCountry("USA");
        customer.setAddress(customerAddress);
        
        customer.setRoles(new HashSet<>());
        customer.addRole(User.UserRole.ROLE_CUSTOMER);
        
        userRepository.saveAll(List.of(admin, customer));
    }
    
    private void createVehicles() {
        // Create sample vehicles
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setVin("1HGCM82633A123456");
        vehicle1.setMake("Toyota");
        vehicle1.setModel("Camry");
        vehicle1.setModelYear(2022);
        vehicle1.setColor("Blue");
        vehicle1.setPrice(new BigDecimal("25000.00"));
        vehicle1.setMileage(15000);
        vehicle1.setFuelType(Vehicle.FuelType.GASOLINE);
        vehicle1.setTransmissionType(Vehicle.TransmissionType.AUTOMATIC);
        vehicle1.setStatus(Vehicle.VehicleStatus.AVAILABLE);
        vehicle1.setDescription("A reliable and fuel-efficient sedan perfect for daily commuting. Features include keyless entry, backup camera, and Bluetooth connectivity.");
        vehicle1.setFeatures(Arrays.asList("Backup Camera", "Bluetooth", "Keyless Entry", "Cruise Control"));
        vehicle1.setImageUrls(Arrays.asList(
            "/dealership/images/vehicles/camry1.jpg", 
            "/dealership/images/vehicles/camry2.jpg"
        ));
        
        Vehicle vehicle2 = new Vehicle();
        vehicle2.setVin("5XYZU3LB7DG123457");
        vehicle2.setMake("Honda");
        vehicle2.setModel("CR-V");
        vehicle2.setModelYear(2021);
        vehicle2.setColor("Silver");
        vehicle2.setPrice(new BigDecimal("28500.00"));
        vehicle2.setMileage(22000);
        vehicle2.setFuelType(Vehicle.FuelType.GASOLINE);
        vehicle2.setTransmissionType(Vehicle.TransmissionType.CVT);
        vehicle2.setStatus(Vehicle.VehicleStatus.AVAILABLE);
        vehicle2.setDescription("A spacious and versatile SUV with excellent safety features. Perfect for families or those who need extra cargo space.");
        vehicle2.setFeatures(Arrays.asList("All-Wheel Drive", "Apple CarPlay", "Android Auto", "Lane Keeping Assist", "Adaptive Cruise Control"));
        vehicle2.setImageUrls(Arrays.asList(
            "/dealership/images/vehicles/crv1.jpg", 
            "/dealership/images/vehicles/crv2.jpg"
        ));
        
        Vehicle vehicle3 = new Vehicle();
        vehicle3.setVin("3VWSG7AJ5BM123458");
        vehicle3.setMake("Tesla");
        vehicle3.setModel("Model 3");
        vehicle3.setModelYear(2023);
        vehicle3.setColor("Red");
        vehicle3.setPrice(new BigDecimal("48000.00"));
        vehicle3.setMileage(5000);
        vehicle3.setFuelType(Vehicle.FuelType.ELECTRIC);
        vehicle3.setTransmissionType(Vehicle.TransmissionType.AUTOMATIC);
        vehicle3.setStatus(Vehicle.VehicleStatus.AVAILABLE);
        vehicle3.setDescription("The Tesla Model 3 is an all-electric four-door sedan with minimalist styling and cutting-edge technology. Features include autopilot capabilities and over-the-air updates.");
        vehicle3.setFeatures(Arrays.asList("Autopilot", "360-degree Camera", "Glass Roof", "15-inch Touchscreen", "Wireless Charging"));
        vehicle3.setImageUrls(Arrays.asList(
            "/dealership/images/vehicles/tesla1.jpg", 
            "/dealership/images/vehicles/tesla2.jpg"
        ));
        
        vehicleRepository.saveAll(List.of(vehicle1, vehicle2, vehicle3));
    }
} 