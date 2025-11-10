package com.customer.client;

import com.customer.dto.ServiceRecordDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

// "maintenance-service" là tên service đã đăng ký với Eureka/Consul
@FeignClient(name = "maintenance-service")
public interface MaintenanceServiceClient {

    // Endpoint này phải tồn tại bên MAINTENANCE-SERVICE
    @GetMapping("/api/v1/servicerecords/vehicle/{vehicleId}")
    List<ServiceRecordDto> getServiceRecordsByVehicleId(@PathVariable("vehicleId") Long vehicleId);
}
