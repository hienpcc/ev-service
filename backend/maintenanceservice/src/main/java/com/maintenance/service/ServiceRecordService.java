package com.maintenance.service;

import com.maintenance.client.AppointmentServiceClient;
import com.maintenance.client.AppointmentStatusUpdateDto;
import com.maintenance.dto.ServiceRecordDto;
import com.maintenance.dto.ServiceUsageDto;
import com.maintenance.entity.ServiceRecord;
import com.maintenance.entity.ServiceUsage;
import com.maintenance.entity.SparePart;
import com.maintenance.entity.Technician;
import com.maintenance.repository.ServiceRecordRepository;
import com.maintenance.repository.ServiceUsageRepository;
import com.maintenance.repository.SparePartRepository;
import com.maintenance.repository.TechnicianRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceRecordService {

    private final ServiceRecordRepository serviceRecordRepository;
    private final TechnicianRepository technicianRepository;
    private final AppointmentServiceClient appointmentServiceClient;
    private final SparePartService sparePartService; // Dùng để trừ kho
    private final SparePartRepository sparePartRepository; // Dùng để lấy entity
    private final ServiceUsageRepository serviceUsageRepository; // Dùng để lưu

    /**
     * Lấy tất cả hồ sơ dịch vụ cho một xe cụ thể.
     * Đây là phương thức chính phục vụ cho Feign Client từ customer-service.
     */
    @Transactional(readOnly = true)
    public List<ServiceRecordDto> findAllByVehicleId(Long vehicleId) {
        List<ServiceRecord> records = serviceRecordRepository.findByVehicleIdOrderByDateDesc(vehicleId);
        return records.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Lấy chi tiết một hồ sơ dịch vụ.
     */
    @Transactional(readOnly = true)
    public ServiceRecordDto getServiceRecordById(Long recordId) {
        ServiceRecord record = serviceRecordRepository.findById(recordId)
                .orElseThrow(() -> new EntityNotFoundException("ServiceRecord not found: " + recordId));
        return mapToDto(record);
    }

    // (Bạn sẽ thêm các phương thức createServiceRecord, updateServiceRecord... ở đây)
    @Transactional
    public ServiceRecordDto createServiceRecord(ServiceRecordDto dto) {
        // 1. Tìm Kỹ thuật viên (như cũ)
        Technician technician = null;
        if (dto.getTechnicianId() != null) {
            technician = technicianRepository.findById(dto.getTechnicianId())
                    .orElseThrow(() -> new EntityNotFoundException("Technician not found"));
        }

        // 2. Tạo và lưu ServiceRecord (chưa có phụ tùng)
        ServiceRecord record = new ServiceRecord();
        record.setVehicleId(dto.getVehicleId());
        record.setAppointmentId(dto.getAppointmentId());
        record.setTechnician(technician);
        record.setDate(dto.getDate() != null ? dto.getDate() : java.time.LocalDate.now());
        record.setDescription(dto.getDescription());
        record.setNotes(dto.getNotes());
        record.setCost(dto.getCost());

        ServiceRecord savedRecord = serviceRecordRepository.save(record); // Lưu để lấy ID

        // 3. Xử lý phụ tùng đã sử dụng
        List<ServiceUsage> partsUsedList = new ArrayList<>();
        if (dto.getPartsUsed() != null && !dto.getPartsUsed().isEmpty()) {
            for (ServiceUsageDto usageDto : dto.getPartsUsed()) {
                // a. Trừ kho (gọi service đã viết ở trên)
                sparePartService.decrementStock(usageDto.getPartId(), usageDto.getQuantityUsed());

                // b. Tạo entity ServiceUsage để lưu lịch sử
                SparePart part = sparePartRepository.findById(usageDto.getPartId())
                        .orElseThrow(() -> new EntityNotFoundException("SparePart not found"));

                ServiceUsage usage = new ServiceUsage();
                usage.setServiceRecord(savedRecord); // Gán vào record vừa tạo
                usage.setSparePart(part);
                usage.setQuantityUsed(usageDto.getQuantityUsed());

                partsUsedList.add(serviceUsageRepository.save(usage));
            }
        }
        savedRecord.setPartsUsed(partsUsedList); // Cập nhật record với danh sách phụ tùng

        // 4. GỌI NGƯỢC APPOINTMENT-SERVICE (như cũ)
        try {
            // ... (code gọi Feign client để set status 'COMPLETED')
        } catch (Exception e) {
            // ... (xử lý lỗi)
        }

        // 5. Trả về DTO (đã cập nhật hàm mapToDto)
        return mapToDto(savedRecord);
    }


    // Phương thức helper để map Entity sang DTO
    private ServiceRecordDto mapToDto(ServiceRecord record) {
        ServiceRecordDto dto = new ServiceRecordDto();
        dto.setRecordId(record.getRecordId());
        dto.setVehicleId(record.getVehicleId());
        dto.setAppointmentId(record.getAppointmentId());
        dto.setDescription(record.getDescription());
        dto.setNotes(record.getNotes());
        dto.setDate(record.getDate());
        dto.setCost(record.getCost());

        if (record.getTechnician() != null) {
            dto.setTechnicianId(record.getTechnician().getTechnicianId());
            dto.setTechnicianName(record.getTechnician().getName());
        }

        // Map danh sách phụ tùng đã dùng
        if (record.getPartsUsed() != null) {
            List<ServiceUsageDto> usageDtos = record.getPartsUsed().stream()
                    .map(usage -> {
                        ServiceUsageDto usageDto = new ServiceUsageDto();
                        usageDto.setUsageId(usage.getUsageId());
                        usageDto.setRecordId(record.getRecordId());
                        usageDto.setPartId(usage.getSparePart().getPartId());
                        usageDto.setQuantityUsed(usage.getQuantityUsed());
                        usageDto.setPartName(usage.getSparePart().getName());
                        usageDto.setPartPrice(usage.getSparePart().getPrice());
                        return usageDto;
                    }).collect(Collectors.toList());
            dto.setPartsUsed(usageDtos);
        }
        return dto;
    }
}
