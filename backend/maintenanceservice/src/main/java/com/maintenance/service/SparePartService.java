package com.maintenance.service;

import com.maintenance.dto.SparePartDto;
import com.maintenance.entity.SparePart;
import com.maintenance.repository.SparePartRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SparePartService {

    private final SparePartRepository sparePartRepository;
    // (Có thể inject KafkaTemplate ở đây để gửi cảnh báo hết hàng)

    // --- CRUD cơ bản cho quản lý kho ---

    @Transactional
    public SparePartDto createSparePart(SparePartDto dto) {
        SparePart part = mapToEntity(dto);
        SparePart savedPart = sparePartRepository.save(part);
        return mapToDto(savedPart);
    }

    @Transactional(readOnly = true)
    public List<SparePartDto> getAllSpareParts() {
        return sparePartRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public SparePartDto updateSparePart(Long partId, SparePartDto dto) {
        SparePart part = sparePartRepository.findById(partId)
                .orElseThrow(() -> new EntityNotFoundException("SparePart not found"));

        part.setName(dto.getName());
        part.setDescription(dto.getDescription());
        part.setQuantityInStock(dto.getQuantityInStock());
        part.setWarnStockLevel(dto.getWarnStockLevel());
        part.setPrice(dto.getPrice());

        SparePart updatedPart = sparePartRepository.save(part);
        return mapToDto(updatedPart);
    }

    // --- Logic nghiệp vụ quan trọng ---

    /**
     * Giảm số lượng tồn kho khi một phụ tùng được sử dụng.
     * Được gọi bởi ServiceRecordService.
     */
    @Transactional
    public void decrementStock(Long partId, int quantityToUse) {
        SparePart part = sparePartRepository.findById(partId)
                .orElseThrow(() -> new EntityNotFoundException("SparePart not found: " + partId));

        int newStock = part.getQuantityInStock() - quantityToUse;
        if (newStock < 0) {
            // Không đủ hàng trong kho
            throw new IllegalStateException("Not enough stock for part: " + part.getName());
        }

        part.setQuantityInStock(newStock);

        // Kiểm soát lượng tồn phụ tùng tối thiểu
        if (newStock <= part.getWarnStockLevel()) {
            // GỬI CẢNH BÁO
            System.out.println("CẢNH BÁO: Phụ tùng " + part.getName() + " sắp hết hàng!");
            // (Nơi lý tưởng để gửi sự kiện Kafka/RabbitMQ đến NOTIFICATION-SERVICE)
        }

        sparePartRepository.save(part);
    }

    // --- Mappers ---
    private SparePartDto mapToDto(SparePart part) {
        SparePartDto dto = new SparePartDto();
        dto.setPartId(part.getPartId());
        dto.setName(part.getName());
        dto.setDescription(part.getDescription());
        dto.setQuantityInStock(part.getQuantityInStock());
        dto.setWarnStockLevel(part.getWarnStockLevel());
        dto.setPrice(part.getPrice());
        return dto;
    }

    private SparePart mapToEntity(SparePartDto dto) {
        SparePart part = new SparePart();
        part.setName(dto.getName());
        part.setDescription(dto.getDescription());
        part.setQuantityInStock(dto.getQuantityInStock());
        part.setWarnStockLevel(dto.getWarnStockLevel());
        part.setPrice(dto.getPrice());
        return part;
    }
}
