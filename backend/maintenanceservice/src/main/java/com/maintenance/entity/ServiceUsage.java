package com.maintenance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "service_usage")
@Getter
@Setter
public class ServiceUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usage_id")
    private Long usageId;

    // Liên kết với Hồ sơ dịch vụ (nhiều 'sử dụng' cho một 'hồ sơ')
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private ServiceRecord serviceRecord;

    // Liên kết với Phụ tùng (nhiều 'sử dụng' cho một 'phụ tùng')
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = false)
    private SparePart sparePart;

    @Column(name = "quantity_used")
    private Integer quantityUsed;
}
