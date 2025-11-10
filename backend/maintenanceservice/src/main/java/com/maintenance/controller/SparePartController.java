package com.maintenance.controller;

import com.maintenance.dto.SparePartDto;
import com.maintenance.service.SparePartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/spareparts")
@RequiredArgsConstructor
public class SparePartController {

    private final SparePartService sparePartService;

    /**
     * [POST] Thêm một phụ tùng mới vào kho.
     */
    @PostMapping
    public ResponseEntity<SparePartDto> createSparePart(@RequestBody SparePartDto dto) {
        return new ResponseEntity<>(sparePartService.createSparePart(dto), HttpStatus.CREATED);
    }

    /**
     * [GET] Lấy tất cả phụ tùng trong kho.
     */
    @GetMapping
    public ResponseEntity<List<SparePartDto>> getAllSpareParts() {
        return ResponseEntity.ok(sparePartService.getAllSpareParts());
    }

    /**
     * [PUT] Cập nhật thông tin/số lượng phụ tùng.
     */
    @PutMapping("/{partId}")
    public ResponseEntity<SparePartDto> updateSparePart(@PathVariable Long partId, @RequestBody SparePartDto dto) {
        return ResponseEntity.ok(sparePartService.updateSparePart(partId, dto));
    }
}
