package com.appointmentservices.repository;

import com.appointmentservices.entity.ServiceCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository để quản lý dữ liệu ServiceCenter (Trung tâm dịch vụ).
 * Cung cấp các phương thức CRUD cơ bản.
 */
@Repository
public interface ServiceCenterRepository extends JpaRepository<ServiceCenter, Long> {

}