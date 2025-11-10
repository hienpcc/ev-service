package com.customer.dto;

import lombok.Data;
import java.util.List;

// DTO này chứa thông tin 1 xe VÀ lịch sử dịch vụ của nó
@Data
public class VehicleProfileDto {
    private Long vehicleId;
    private String vin;
    private String make;
    private String model;
    private Integer year;
    private List<ServiceRecordDto> serviceHistory; // Đây là dữ liệu từ service khác
}