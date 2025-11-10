package com.maintenance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(
        name = "service_record",
        indexes = {
                @Index(name = "idx_vehicle_id", columnList = "vehicle_id")
        }
)
@Getter
@Setter
public class ServiceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    // Đây là ID từ service khác, nên chúng ta chỉ lưu ID
    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    // Liên kết với Kỹ thuật viên trong cùng service này
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technician_id")
    private Technician technician;

    @Column(name = "appointment_id", unique = true)
    private Long appointmentId; // Liên kết với lịch hẹn (từ APPOINTMENT-SERVICE)

    @Column(name = "description")
    private String description;

    @Column(name = "notes")
    private String notes;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "cost")
    private BigDecimal cost;

    @OneToMany(mappedBy = "serviceRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceUsage> partsUsed; // Danh sách phụ tùng đã sử dụng
}