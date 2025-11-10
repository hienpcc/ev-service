package com.customer.controller;

import com.customer.dto.VehicleDto;
import com.customer.service.CustomerManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private final CustomerManagementService customerService;

    public VehicleController(CustomerManagementService customerService) {
        this.customerService = customerService;
    }

    // Thêm xe mới cho khách hàng
    @PostMapping
    public ResponseEntity<VehicleDto> addVehicle(@RequestBody VehicleDto vehicleDto) {
        // Cần customerId trong DTO để biết thêm xe cho ai
        return new ResponseEntity<>(customerService.addVehicle(vehicleDto), HttpStatus.CREATED);
    }

    // Lấy danh sách xe của một khách hàng
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<VehicleDto>> getVehiclesByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getVehiclesForCustomer(customerId));
    }

    // Cập nhật thông tin xe
    @PutMapping("/{vehicleId}")
    public ResponseEntity<VehicleDto> updateVehicle(@PathVariable Long vehicleId, @RequestBody VehicleDto vehicleDto) {
        return ResponseEntity.ok(customerService.updateVehicle(vehicleId, vehicleDto));
    }
}
