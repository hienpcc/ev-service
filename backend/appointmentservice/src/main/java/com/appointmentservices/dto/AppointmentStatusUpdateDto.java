package com.appointmentservices.dto;

import com.appointmentservices.entity.AppointmentStatus;
import lombok.Data;

@Data
public class AppointmentStatusUpdateDto {
    private AppointmentStatus status;
}