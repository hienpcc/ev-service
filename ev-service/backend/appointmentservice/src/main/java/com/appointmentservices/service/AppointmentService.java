package com.appointmentservices.service;

import com.appointmentservices.model.Appointment;
import java.util.List;

public interface AppointmentService {
    Appointment createAppointment(Appointment appointment);
    List<Appointment> getAppointmentsByCustomer(Long customerId);
    Appointment updateStatus(Long appointmentId, String status);
}