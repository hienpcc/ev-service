package com.customer.dto;

import lombok.Data;

@Data
public class VehicleDto {
    private Long vehicleId;
    private String vin;
    private String make;
    private String model;
    private Integer year;
    private Long customerId;
}
