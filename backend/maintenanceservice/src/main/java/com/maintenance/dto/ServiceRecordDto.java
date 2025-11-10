package com.maintenance.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ServiceRecordDto {
    private Long recordId;
    private Long vehicleId;
    private Long technicianId; // Chỉ trả về ID của kỹ thuật viên
    private String technicianName; // Có thể thêm tên cho tiện
    private Long appointmentId;
    private String description;
    private String notes;
    private LocalDate date;
    private BigDecimal cost;
    private List<ServiceUsageDto> partsUsed;
}
