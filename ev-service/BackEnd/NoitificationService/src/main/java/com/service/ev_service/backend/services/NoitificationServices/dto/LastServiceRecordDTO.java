package com.service.ev_service.backend.services.NoitificationServices.dto;

import java.time.LocalDate;

public record LastServiceRecordDTO(
        String recordId,
        String vehicleId,
        LocalDate date
) {}