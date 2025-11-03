package com.appointmentservices.service;

import com.appointmentservices.model.Appointment;
import com.appointmentservices.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository repository;

    @Override
    public Appointment createAppointment(Appointment appointment) {
        appointment.setStatus("Pending");
        appointment.setCreatedAt(LocalDateTime.now());
        return repository.save(appointment);
    }

    @Override
    public List<Appointment> getAppointmentsByCustomer(Long customerId) {
        return repository.findByCustomerId(customerId);
    }

    @Override
    public Appointment updateStatus(Long appointmentId, String status) {
        Appointment appt = repository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appt.setStatus(status);
        return repository.save(appt);
    }
}