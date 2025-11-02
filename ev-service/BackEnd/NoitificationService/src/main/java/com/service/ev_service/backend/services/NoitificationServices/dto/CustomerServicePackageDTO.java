package com.service.ev_service.backend.services.NoitificationServices.dto;

import java.time.LocalDate;

public record CustomerServicePackageDTO(
        Long customerId,
        String packageName,
        LocalDate endDate
) {}