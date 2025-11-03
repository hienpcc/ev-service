package com.appointmentservices.dto;

import java.time.LocalDateTime;

public class AppointmentRequest {

    private Long customerId;
    private Long vehicleId;
    private Long serviceCenterId;
    private Long serviceTypeId;
    private LocalDateTime scheduledDate;
    private String notes;

    public AppointmentRequest() {}

    public AppointmentRequest(Long customerId, Long vehicleId, Long serviceCenterId,
                              Long serviceTypeId, LocalDateTime scheduledDate, String notes) {
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.serviceCenterId = serviceCenterId;
        this.serviceTypeId = serviceTypeId;
        this.scheduledDate = scheduledDate;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }
    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getServiceCenterId() {
        return serviceCenterId;
    }
    public void setServiceCenterId(Long serviceCenterId) {
        this.serviceCenterId = serviceCenterId;
    }

    public Long getServiceTypeId() {
        return serviceTypeId;
    }
    public void setServiceTypeId(Long serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }
    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
