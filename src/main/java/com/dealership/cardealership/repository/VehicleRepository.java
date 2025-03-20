package com.dealership.cardealership.repository;

import com.dealership.cardealership.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface for Vehicle entity operations.
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    List<Vehicle> findByStatus(Vehicle.VehicleStatus status);
    
    Page<Vehicle> findByStatus(Vehicle.VehicleStatus status, Pageable pageable);
    
    long countByStatus(Vehicle.VehicleStatus status);
    
    List<Vehicle> findByMakeIgnoreCase(String make);
    
    Page<Vehicle> findByMake(String make, Pageable pageable);
    
    Page<Vehicle> findByMakeAndStatusOrderByIdDesc(String make, Vehicle.VehicleStatus status, Pageable pageable);
    
    List<Vehicle> findByModelIgnoreCase(String model);
    
    List<Vehicle> findByMakeIgnoreCaseAndModelIgnoreCase(String make, String model);
    
    List<Vehicle> findByModelYearBetween(Integer startYear, Integer endYear);
    
    List<Vehicle> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    @Query("SELECT v FROM Vehicle v WHERE v.vin LIKE %:keyword% OR v.make LIKE %:keyword% OR v.model LIKE %:keyword% OR v.description LIKE %:keyword%")
    Page<Vehicle> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT DISTINCT v.make FROM Vehicle v ORDER BY v.make")
    List<String> findAllMakes();
    
    @Query("SELECT DISTINCT v.model FROM Vehicle v WHERE v.make = ?1 ORDER BY v.model")
    List<String> findAllModelsByMake(String make);
} 