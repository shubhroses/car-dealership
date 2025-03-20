package com.dealership.cardealership.repository;

import com.dealership.cardealership.model.Inquiry;
import com.dealership.cardealership.model.User;
import com.dealership.cardealership.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for Inquiry entity operations.
 */
@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    
    List<Inquiry> findByUser(User user);
    
    List<Inquiry> findByUserOrderByInquiryDateDesc(User user);
    
    List<Inquiry> findByVehicle(Vehicle vehicle);
    
    List<Inquiry> findByStatus(Inquiry.InquiryStatus status);
    
    Page<Inquiry> findByStatus(Inquiry.InquiryStatus status, Pageable pageable);
    
    long countByStatus(Inquiry.InquiryStatus status);
    
    List<Inquiry> findByUserAndStatus(User user, Inquiry.InquiryStatus status);
    
    List<Inquiry> findByStaff(User staff);
    
    @Query("SELECT i FROM Inquiry i WHERE i.message LIKE %:keyword% OR i.user.firstName LIKE %:keyword% OR i.user.lastName LIKE %:keyword% OR i.user.email LIKE %:keyword% OR i.vehicle.make LIKE %:keyword% OR i.vehicle.model LIKE %:keyword%")
    Page<Inquiry> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
} 