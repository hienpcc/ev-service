package com.service.ev_service.backend.services.NoitificationServices.dto;

public record VehicleDTO(
        String vehicleId,
        Long customerId,
        String model,
        int year
) {}