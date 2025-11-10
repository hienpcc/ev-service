package com.appointmentservices.dto;

import lombok.Data;
@Data
public class ServiceCenterDto {
    private Long serviceCenterId;
    private String name;
    private String address;
    private String contactInfo;
}
