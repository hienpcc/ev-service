package com.notificationservice.dto;

import java.time.LocalDate;

public record CustomerServicePackageDTO(
        Long customerId,
        String packageName,
        LocalDate endDate
) {}