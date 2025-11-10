package com.maintenance.repository;

import com.maintenance.entity.ServiceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceRecordRepository extends JpaRepository<ServiceRecord, Long> {

    /**
     * Tìm tất cả các hồ sơ bảo dưỡng dựa trên ID của xe.
     * Đây là phương thức quan trọng mà Feign Client sẽ sử dụng.
     */
    List<ServiceRecord> findByVehicleIdOrderByDateDesc(Long vehicleId);
}
