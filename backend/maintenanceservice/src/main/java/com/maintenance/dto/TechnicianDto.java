package com.maintenance.dto;

import lombok.Data;

@Data
public class TechnicianDto {
    private Long technicianId;

    // ID từ ACCOUNT-SERVICE, bắt buộc khi tạo mới
    private Long accountId;

    private String name;
    private String specialization; // Chuyên môn
    private String certificate;    // Chứng chỉ chuyên môn EV
    private String dailySchedule;  // Lịch làm việc/Phân công ca
    private Long serviceCenterId;  // Thuộc trung tâm dịch vụ nào
}
