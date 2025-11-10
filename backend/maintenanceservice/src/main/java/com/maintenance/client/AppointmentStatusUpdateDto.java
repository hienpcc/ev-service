package com.maintenance.client;

import lombok.Data;

@Data
public class AppointmentStatusUpdateDto {
    private String status; // Gửi dạng String cho đơn giản

    public AppointmentStatusUpdateDto(String status) {
        this.status = status;
    }
}
