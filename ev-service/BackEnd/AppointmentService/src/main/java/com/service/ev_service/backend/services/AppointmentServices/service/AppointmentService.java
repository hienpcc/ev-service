package com.service.ev_service.backend.services.AppointmentServices.service;

import com.service.ev_service.backend.services.AppointmentServices.model.Appointment;
import java.util.List;

public interface AppointmentService {
    Appointment createAppointment(Appointment appointment);
    List<Appointment> getAppointmentsByCustomer(Long customerId);
    Appointment updateStatus(Long appointmentId, String status);
}