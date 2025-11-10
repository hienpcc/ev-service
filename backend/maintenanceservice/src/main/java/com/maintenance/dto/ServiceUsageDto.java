package com.maintenance.dto;

import lombok.Data;

@Data
public class ServiceUsageDto {
    private Long usageId;
    private Long recordId; // Chỉ cần ID
    private Long partId;   // Bắt buộc khi tạo
    private Integer quantityUsed; // Bắt buộc khi tạo

    // Thông tin thêm (tùy chọn)
    private String partName;
    private java.math.BigDecimal partPrice;
}