package com.notificationservice.dto;

import java.time.LocalDate;

public record LastServiceRecordDTO(
        String recordId,
        String vehicleId,
        LocalDate date
) {}