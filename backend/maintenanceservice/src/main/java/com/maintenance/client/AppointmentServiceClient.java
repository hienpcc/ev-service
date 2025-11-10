package com.maintenance.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

// "appointment-service" là tên đã đăng ký trên Eureka
@FeignClient(name = "appointment-service")
public interface AppointmentServiceClient {

    // Đường dẫn và phương thức phải khớp với API bên AppointmentController
    @PatchMapping("/api/v1/appointments/{appointmentId}/status")
    void updateAppointmentStatus(
            @PathVariable("appointmentId") Long appointmentId,
            @RequestBody AppointmentStatusUpdateDto statusUpdateDto
    );
}