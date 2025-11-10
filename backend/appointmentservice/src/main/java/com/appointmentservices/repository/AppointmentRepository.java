package com.appointmentservices.repository;

import com.appointmentservices.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Lấy lịch hẹn cho Khách hàng (Chức năng 1b)
    List<Appointment> findByCustomerIdOrderByScheduleDateDesc(Long customerId);

    // Lấy lịch hẹn cho Trung tâm dịch vụ (Chức năng 2b)
    List<Appointment> findByServiceCenterServiceCenterIdOrderByScheduleDateAsc(Long serviceCenterId);

    // Lấy lịch hẹn cho Kỹ thuật viên (Chức năng 2b)
    List<Appointment> findByTechnicianIdOrderByScheduleDateAsc(Long technicianId);
}
