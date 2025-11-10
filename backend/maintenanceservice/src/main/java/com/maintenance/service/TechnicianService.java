package com.maintenance.service;

import com.maintenance.dto.TechnicianDto;
import com.maintenance.entity.Technician;
import com.maintenance.repository.TechnicianRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TechnicianService {

    private final TechnicianRepository technicianRepository;

    /**
     * Tạo một Kỹ thuật viên mới.
     * Yêu cầu accountId đã tồn tại bên Account-Service.
     */
    @Transactional
    public TechnicianDto createTechnician(TechnicianDto dto) {
        // (Trong thực tế, bạn nên dùng Feign Client
        // gọi sang Account-Service để kiểm tra accountId có hợp lệ không)

        Technician technician = new Technician();
        technician.setAccountId(dto.getAccountId()); // Gán khóa ngoại
        technician.setName(dto.getName());
        technician.setSpecialization(dto.getSpecialization());
        technician.setCertificate(dto.getCertificate());
        technician.setDailySchedule(dto.getDailySchedule());
        technician.setServiceCenterId(dto.getServiceCenterId());

        Technician savedTechnician = technicianRepository.save(technician);
        return mapToDto(savedTechnician);
    }

    /**
     * Cập nhật thông tin Kỹ thuật viên.
     * Đây là nơi "Quản lý chứng chỉ" và "Phân công ca/lịch" diễn ra.
     */
    @Transactional
    public TechnicianDto updateTechnician(Long technicianId, TechnicianDto dto) {
        Technician technician = technicianRepository.findById(technicianId)
                .orElseThrow(() -> new EntityNotFoundException("Technician not found: " + technicianId));

        technician.setName(dto.getName());
        technician.setSpecialization(dto.getSpecialization());
        technician.setCertificate(dto.getCertificate()); // Cập nhật chứng chỉ
        technician.setDailySchedule(dto.getDailySchedule()); // Cập nhật lịch làm việc
        technician.setServiceCenterId(dto.getServiceCenterId());
        // (Không cho phép cập nhật accountId)

        Technician updatedTechnician = technicianRepository.save(technician);
        return mapToDto(updatedTechnician);
    }

    /**
     * Lấy thông tin chi tiết một Kỹ thuật viên.
     */
    @Transactional(readOnly = true)
    public TechnicianDto getTechnicianById(Long technicianId) {
        Technician technician = technicianRepository.findById(technicianId)
                .orElseThrow(() -> new EntityNotFoundException("Technician not found: " + technicianId));
        return mapToDto(technician);
    }

    /**
     * Lấy danh sách tất cả Kỹ thuật viên.
     */
    @Transactional(readOnly = true)
    public List<TechnicianDto> getAllTechnicians() {
        return technicianRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Xóa Kỹ thuật viên.
     * (Lưu ý: Bạn cũng cần gọi Account-Service để vô hiệu hóa tài khoản)
     */
    @Transactional
    public void deleteTechnician(Long technicianId) {
        if (!technicianRepository.existsById(technicianId)) {
            throw new EntityNotFoundException("Technician not found: " + technicianId);
        }
        technicianRepository.deleteById(technicianId);
        // (Gọi Feign Client sang Account-Service để
        // vô hiệu hóa/xóa Account tương ứng)
    }

    // --- Mapper Helper ---
    private TechnicianDto mapToDto(Technician tech) {
        TechnicianDto dto = new TechnicianDto();
        dto.setTechnicianId(tech.getTechnicianId());
        dto.setAccountId(tech.getAccountId());
        dto.setName(tech.getName());
        dto.setSpecialization(tech.getSpecialization());
        dto.setCertificate(tech.getCertificate());
        dto.setDailySchedule(tech.getDailySchedule());
        dto.setServiceCenterId(tech.getServiceCenterId());
        return dto;
    }
}
