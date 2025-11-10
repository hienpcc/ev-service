package com.maintenance.controller;

import com.maintenance.dto.TechnicianDto;
import com.maintenance.service.TechnicianService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/technicians")
@RequiredArgsConstructor
public class TechnicianController {

    private final TechnicianService technicianService;

    /**
     * [POST] Tạo một Kỹ thuật viên mới.
     * Yêu cầu: Đã tạo Account bên Account-Service trước.
     */
    @PostMapping
    public ResponseEntity<TechnicianDto> createTechnician(@RequestBody TechnicianDto dto) {
        TechnicianDto createdTechnician = technicianService.createTechnician(dto);
        return new ResponseEntity<>(createdTechnician, HttpStatus.CREATED);
    }

    /**
     * [GET] Lấy danh sách tất cả Kỹ thuật viên.
     */
    @GetMapping
    public ResponseEntity<List<TechnicianDto>> getAllTechnicians() {
        return ResponseEntity.ok(technicianService.getAllTechnicians());
    }

    /**
     * [GET] Lấy thông tin chi tiết một Kỹ thuật viên.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TechnicianDto> getTechnicianById(@PathVariable Long id) {
        return ResponseEntity.ok(technicianService.getTechnicianById(id));
    }

    /**
     * [PUT] Cập nhật thông tin Kỹ thuật viên (chứng chỉ, lịch làm việc...).
     */
    @PutMapping("/{id}")
    public ResponseEntity<TechnicianDto> updateTechnician(
            @PathVariable Long id,
            @RequestBody TechnicianDto dto) {
        return ResponseEntity.ok(technicianService.updateTechnician(id, dto));
    }

    /**
     * [DELETE] Xóa một Kỹ thuật viên.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnician(@PathVariable Long id) {
        technicianService.deleteTechnician(id);
        return ResponseEntity.noContent().build();
    }
}
