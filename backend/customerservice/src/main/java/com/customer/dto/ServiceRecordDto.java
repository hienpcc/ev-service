package com.customer.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

// Đây là DTO "giả" trong customer-service
// để hứng dữ liệu trả về từ maintenance-service.
// Các trường phải khớp với DTO bên maintenance-service.
@Data
public class ServiceRecordDto {
    private Long recordId;
    private Long vehicleId;
    private Long technicianId;
    private String description;
    private String notes;
    private LocalDate date;
    private BigDecimal cost;
}
