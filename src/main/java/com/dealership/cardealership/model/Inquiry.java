package com.dealership.cardealership.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Represents a customer inquiry about a vehicle.
 */
@Entity
@Table(name = "inquiries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inquiry {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private LocalDateTime inquiryDate = LocalDateTime.now();
    
    @Column(length = 2000)
    private String message;
    
    @Enumerated(EnumType.STRING)
    private InquiryStatus status = InquiryStatus.NEW;
    
    private LocalDateTime responseDate;
    
    @Column(length = 2000)
    private String response;
    
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private User staff;
    
    public enum InquiryStatus {
        NEW, IN_PROGRESS, RESPONDED, CLOSED
    }
} 