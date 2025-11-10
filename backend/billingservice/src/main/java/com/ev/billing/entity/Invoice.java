package com.ev.billing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "invoice",
        indexes = {
                @Index(name = "idx_customer_id", columnList = "customer_id")
        }
)
@Getter
@Setter
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long invoiceId;

    // ID từ các service khác
    @Column(name = "record_id", unique = true)
    private Long recordId; // Liên kết với ServiceRecord (nguyên nhân tạo hóa đơn)

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount; // Tổng tiền

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InvoiceStatus status; // Trạng thái (PAID, UNPAID)

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "payment_method")
    private String paymentMethod;

    @PrePersist
    protected void onCreate() {
        if (date == null) date = LocalDate.now();
        if (status == null) status = InvoiceStatus.UNPAID; // Mặc định là chưa thanh toán
    }
}
