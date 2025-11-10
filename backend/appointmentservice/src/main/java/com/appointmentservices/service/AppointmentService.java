package com.appointmentservices.service;

import com.appointmentservices.dto.AppointmentDto;
import com.appointmentservices.dto.AppointmentRequestDto;
import com.appointmentservices.dto.ServiceCenterDto;
import com.appointmentservices.dto.ServiceTypeDto;
import com.appointmentservices.entity.Appointment;
import com.appointmentservices.entity.AppointmentStatus;
import com.appointmentservices.entity.ServiceCenter;
import com.appointmentservices.entity.ServiceType;
import com.appointmentservices.repository.AppointmentRepository;
import com.appointmentservices.repository.ServiceCenterRepository;
import com.appointmentservices.repository.ServiceTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ServiceCenterRepository serviceCenterRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    // (Bạn cũng cần 1 Mapper, ở đây tôi map thủ công)

    // --- Chức năng 1b: Khách hàng đặt lịch ---
    @Transactional
    public AppointmentDto createAppointment(AppointmentRequestDto requestDto) {
        // Lấy các entity liên quan
        ServiceCenter center = serviceCenterRepository.findById(requestDto.getServiceCenterId())
                .orElseThrow(() -> new EntityNotFoundException("ServiceCenter not found"));
        ServiceType type = serviceTypeRepository.findById(requestDto.getServiceTypeId())
                .orElseThrow(() -> new EntityNotFoundException("ServiceType not found"));

        Appointment appointment = new Appointment();
        appointment.setCustomerId(requestDto.getCustomerId());
        appointment.setVehicleId(requestDto.getVehicleId());
        appointment.setServiceCenter(center);
        appointment.setServiceType(type);
        appointment.setScheduleDate(requestDto.getScheduleDate());
        appointment.setNotes(requestDto.getNotes());
        // Trạng thái PENDING sẽ được gán tự động bởi @PrePersist

        Appointment savedApp = appointmentRepository.save(appointment);
        return mapToDto(savedApp);
    }

    // --- Chức năng 1b & 2b: Xem lịch hẹn ---
    @Transactional(readOnly = true)
    public AppointmentDto getAppointmentById(Long id) {
        Appointment app = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));
        return mapToDto(app);
    }

    @Transactional(readOnly = true)
    public List<AppointmentDto> getAppointmentsForCustomer(Long customerId) {
        return appointmentRepository.findByCustomerIdOrderByScheduleDateDesc(customerId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // --- Chức năng 2b: Staff/Admin quản lý ---
    @Transactional(readOnly = true)
    public List<AppointmentDto> getAppointmentsForServiceCenter(Long centerId) {
        return appointmentRepository.findByServiceCenterServiceCenterIdOrderByScheduleDateAsc(centerId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AppointmentDto updateAppointmentByStaff(Long appointmentId, AppointmentRequestDto requestDto) {
        Appointment app = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        // Staff có thể gán Kỹ thuật viên và đổi Trạng thái
        app.setTechnicianId(requestDto.getTechnicianId());
        app.setStatus(requestDto.getStatus());

        // (Có thể cập nhật các trường khác nếu cần)
        app.setScheduleDate(requestDto.getScheduleDate());

        Appointment updatedApp = appointmentRepository.save(app);
        return mapToDto(updatedApp);
    }

    // --- Helper Mapper ---
    private AppointmentDto mapToDto(Appointment app) {
        AppointmentDto dto = new AppointmentDto();
        dto.setAppointmentId(app.getAppointmentId());
        dto.setCustomerId(app.getCustomerId());
        dto.setVehicleId(app.getVehicleId());
        dto.setTechnicianId(app.getTechnicianId());
        dto.setStatus(app.getStatus());
        dto.setScheduleDate(app.getScheduleDate());
        dto.setCreatedAt(app.getCreatedAt());
        dto.setNotes(app.getNotes());

        // Map ServiceCenter
        ServiceCenterDto centerDto = new ServiceCenterDto();
        centerDto.setServiceCenterId(app.getServiceCenter().getServiceCenterId());
        centerDto.setName(app.getServiceCenter().getName());
        centerDto.setAddress(app.getServiceCenter().getAddress());
        dto.setServiceCenter(centerDto);

        // Map ServiceType
        ServiceTypeDto typeDto = new ServiceTypeDto();
        typeDto.setServiceTypeId(app.getServiceType().getServiceTypeId());
        typeDto.setName(app.getServiceType().getName());
        typeDto.setDescription(app.getServiceType().getDescription());
        dto.setServiceType(typeDto);

        return dto;
    }
    @Transactional
    public AppointmentDto updateAppointmentStatus(Long appointmentId, AppointmentStatus newStatus) {
        Appointment app = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        // (Có thể thêm logic kiểm tra luồng status,
        // ví dụ: không thể từ PENDING sang COMPLETED)
        app.setStatus(newStatus);

        Appointment updatedApp = appointmentRepository.save(app);

        // BƯỚC NÂNG CAO (Gửi sự kiện Kafka):
        // Gửi thông báo khi trạng thái thay đổi
        // String topic = "appointment_status_updates";
        // String message = "{\"appointmentId\": " + updatedApp.getAppointmentId() + ", \"status\": \"" + newStatus + "\"}";
        // kafkaTemplate.send(topic, message);
        // (Việc này cần thêm dependency 'spring-kafka' và cấu hình)

        return mapToDto(updatedApp);
    }
}