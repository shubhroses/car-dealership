package com.dealership.cardealership.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a vehicle in the dealership inventory.
 */
@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String vin;
    
    private String make;
    
    private String model;
    
    @Column(name = "model_year")
    private Integer modelYear;
    
    private String color;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    
    private Integer mileage;
    
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;
    
    @Enumerated(EnumType.STRING)
    private TransmissionType transmissionType;
    
    @Enumerated(EnumType.STRING)
    private VehicleStatus status = VehicleStatus.AVAILABLE;
    
    @Column(length = 2000)
    private String description;
    
    @ElementCollection
    @CollectionTable(name = "vehicle_features", joinColumns = @JoinColumn(name = "vehicle_id"))
    @Column(name = "feature")
    private List<String> features = new ArrayList<>();
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "vehicle_images", joinColumns = @JoinColumn(name = "vehicle_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();
    
    public enum FuelType {
        GASOLINE, DIESEL, ELECTRIC, HYBRID, PLUGIN_HYBRID
    }
    
    public enum TransmissionType {
        AUTOMATIC, MANUAL, SEMI_AUTOMATIC, CVT
    }
    
    public enum VehicleStatus {
        AVAILABLE, RESERVED, SOLD, MAINTENANCE
    }
} 