package com.maintenance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "technician")
@Getter
@Setter
public class Technician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "technician_id")
    private Long technicianId;

    @Column(name = "account_id", unique = true, nullable = false)
    private Long accountId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "specialization")
    private String specialization; // Chuyên môn

    @Column(name = "certificate")
    private String certificate; // Chứng chỉ

    @Column(name = "daily_schedule")
    private String dailySchedule; // Lịch làm việc

    // Liên kết với ServiceCenter (nếu ServiceCenter là entity riêng)
    @Column(name = "service_center_id")
    private Long serviceCenterId;
}
