package com.appointmentservices.dto;

import com.appointmentservices.entity.AppointmentStatus;
import lombok.Data;
import java.time.LocalDateTime;

// DTO đầy đủ để trả về thông tin chi tiết
@Data
public class AppointmentDto {
    private Long appointmentId;
    private Long customerId;
    private Long vehicleId;
    private Long technicianId;
    private ServiceCenterDto serviceCenter; // Trả về thông tin chi tiết
    private ServiceTypeDto serviceType;     // Trả về thông tin chi tiết
    private AppointmentStatus status;
    private LocalDateTime scheduleDate;
    private LocalDateTime createdAt;
    private String notes;
}
