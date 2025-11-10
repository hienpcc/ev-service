package com.maintenance.controller;

import com.maintenance.dto.ServiceRecordDto;
import com.maintenance.service.ServiceRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/servicerecords")
public class ServiceRecordController {

    private final ServiceRecordService serviceRecordService;


    // (Constructor injection)

    // Endpoint mà CUSTOMER-SERVICE gọi đến
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<ServiceRecordDto>> getRecordsForVehicle(@PathVariable Long vehicleId) {
        List<ServiceRecordDto> records = serviceRecordService.findAllByVehicleId(vehicleId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<ServiceRecordDto> getRecordById(@PathVariable Long recordId) {
        return ResponseEntity.ok(serviceRecordService.getServiceRecordById(recordId));
    }

    @PostMapping
    public ResponseEntity<ServiceRecordDto> createRecord(@RequestBody ServiceRecordDto dto) {
        // DTO này cần chứa appointmentId, technicianId, description, cost...
        ServiceRecordDto createdDto = serviceRecordService.createServiceRecord(dto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }
    // ... các API khác của Maintenance Service (tạo record, v.v.)
}
