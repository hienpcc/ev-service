package com.appointmentservices.repository;

import com.appointmentservices.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository để quản lý dữ liệu ServiceType (Loại hình dịch vụ).
 * Cung cấp các phương thức CRUD cơ bản.
 */
@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {

    // Spring Data JPA sẽ tự động tạo các phương thức findById, save, findAll, ...

    // Ví dụ về một phương thức truy vấn tùy chỉnh:
    // Optional<ServiceType> findByName(String name);
}
