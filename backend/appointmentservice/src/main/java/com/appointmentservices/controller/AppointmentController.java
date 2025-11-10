package com.appointmentservices.controller;

import com.appointmentservices.dto.AppointmentDto;
import com.appointmentservices.dto.AppointmentRequestDto;
import com.appointmentservices.dto.AppointmentStatusUpdateDto;
import com.appointmentservices.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // === API cho Khách hàng (Chức năng 1b) ===

    /**
     * [POST] Khách hàng tạo lịch hẹn mới.
     */
    @PostMapping
    public ResponseEntity<AppointmentDto> createAppointment(@RequestBody AppointmentRequestDto requestDto) {
        // TODO: Cần xác thực customerId (thường lấy từ JWT token)
        AppointmentDto createdDto = appointmentService.createAppointment(requestDto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    /**
     * [GET] Khách hàng xem tất cả lịch hẹn của mình.
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AppointmentDto>> getCustomerAppointments(@PathVariable Long customerId) {
        // TODO: Cần xác thực customerId
        return ResponseEntity.ok(appointmentService.getAppointmentsForCustomer(customerId));
    }

    // === API cho Staff/Admin (Chức năng 2b) ===

    /**
     * [GET] Staff xem tất cả lịch hẹn tại trung tâm của mình.
     */
    @GetMapping("/center/{centerId}")
    public ResponseEntity<List<AppointmentDto>> getCenterAppointments(@PathVariable Long centerId) {
        // TODO: Cần xác thực Staff thuộc centerId này
        return ResponseEntity.ok(appointmentService.getAppointmentsForServiceCenter(centerId));
    }

    /**
     * [PUT] Staff cập nhật lịch hẹn (gán thợ, đổi trạng thái).
     */
    @PutMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDto> updateAppointment(
            @PathVariable Long appointmentId,
            @RequestBody AppointmentRequestDto requestDto) {
        // TODO: Cần xác thực Staff
        AppointmentDto updatedDto = appointmentService.updateAppointmentByStaff(appointmentId, requestDto);
        return ResponseEntity.ok(updatedDto);
    }

    // === API chung ===

    /**
     * [GET] Lấy chi tiết một lịch hẹn.
     */
    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDto> getAppointmentDetails(@PathVariable Long appointmentId) {
        // TODO: Cần xác thực (Customer sở hữu hoặc Staff liên quan)
        return ResponseEntity.ok(appointmentService.getAppointmentById(appointmentId));
    }
    @PatchMapping("/{appointmentId}/status")
    public ResponseEntity<AppointmentDto> updateAppointmentStatus(
            @PathVariable Long appointmentId,
            @RequestBody AppointmentStatusUpdateDto statusUpdateDto) {

        AppointmentDto updatedDto = appointmentService.updateAppointmentStatus(
                appointmentId,
                statusUpdateDto.getStatus()
        );
        return ResponseEntity.ok(updatedDto);
    }
}