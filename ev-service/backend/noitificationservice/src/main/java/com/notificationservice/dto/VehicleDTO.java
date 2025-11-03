package com.notificationservice.dto;

public record VehicleDTO(
        String vehicleId,
        Long customerId,
        String model,
        int year
) {}
