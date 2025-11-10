package com.appointmentservices.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "appointment",
        indexes = {
                @Index(name = "idx_customer_id", columnList = "customer_id"),
                @Index(name = "idx_vehicle_id", columnList = "vehicle_id"),
                @Index(name = "idx_technician_id", columnList = "technician_id")
        }
)
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long appointmentId;

    // ID từ các service khác
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @Column(name = "technician_id")
    private Long technicianId; // Sẽ được gán bởi Staff/Admin

    // Liên kết nội bộ trong service
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_center_id", nullable = false)
    private ServiceCenter serviceCenter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_type_id", nullable = false)
    private ServiceType serviceType;

    // Trạng thái (chờ – đang bảo dưỡng – hoàn tất)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status;

    @Column(name = "schedule_date", nullable = false)
    private LocalDateTime scheduleDate; // Ngày giờ hẹn

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "notes")
    private String notes; // Ghi chú của khách hàng

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = AppointmentStatus.PENDING; // Trạng thái mặc định khi tạo
        }
    }
}

