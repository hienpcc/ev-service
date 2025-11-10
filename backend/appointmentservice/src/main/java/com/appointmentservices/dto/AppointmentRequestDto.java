package com.appointmentservices.dto;

import com.appointmentservices.entity.AppointmentStatus;
import lombok.Data;
import java.time.LocalDateTime;

// DTO dùng khi Khách hàng/Staff tạo hoặc cập nhật lịch hẹn
@Data
public class AppointmentRequestDto {
    private Long customerId; // Bắt buộc khi tạo
    private Long vehicleId;  // Bắt buộc khi tạo
    private Long serviceCenterId; // Bắt buộc khi tạo
    private Long serviceTypeId;   // Bắt buộc khi tạo
    private Long technicianId; // (Tùy chọn, do Staff gán)
    private AppointmentStatus status; // (Tùy chọn, do Staff gán)
    private LocalDateTime scheduleDate; // Bắt buộc khi tạo
    private String notes;
}
