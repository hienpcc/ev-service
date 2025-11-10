package com.customer.repository;

import com.customer.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // Tìm tất cả xe của một khách hàng
    List<Vehicle> findByCustomerCustomerId(Long customerId);
}