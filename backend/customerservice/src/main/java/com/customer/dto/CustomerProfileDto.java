package com.customer.dto;

import lombok.Data;
import java.util.List;

// DTO tổng hợp cuối cùng cho toàn bộ hồ sơ
@Data
public class CustomerProfileDto {
    private Long customerId;
    private String name;
    private String email;
    private String address;
    private List<VehicleProfileDto> vehicles; // Danh sách xe kèm lịch sử dịch vụ
}