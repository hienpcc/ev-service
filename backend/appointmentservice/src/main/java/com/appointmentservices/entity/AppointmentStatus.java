package com.appointmentservices.entity;

public enum AppointmentStatus {
    PENDING,     // Chờ xác nhận
    CONFIRMED,   // Đã xác nhận
    IN_PROGRESS, // Đang bảo dưỡng
    COMPLETED,   // Hoàn tất
    CANCELLED    // Đã hủy
}
