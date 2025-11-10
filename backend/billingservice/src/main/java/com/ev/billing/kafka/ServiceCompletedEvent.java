package com.ev.billing.kafka;

import lombok.Data;
import java.math.BigDecimal;

// DTO này đại diện cho sự kiện mà MAINTENANCE-SERVICE gửi lên
@Data
public class ServiceCompletedEvent {
    private Long recordId;
    private Long customerId;
    private BigDecimal totalCost;
    // (Thêm các trường khác nếu cần)
}
