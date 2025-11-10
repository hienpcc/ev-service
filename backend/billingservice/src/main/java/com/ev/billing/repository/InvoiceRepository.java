package com.ev.billing.repository;

import com.ev.billing.entity.Invoice;
import com.ev.billing.entity.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    // Lấy hóa đơn cho khách hàng (Chức năng 1c)
    List<Invoice> findByCustomerIdOrderByDateDesc(Long customerId);

    // Lấy hóa đơn cho báo cáo tài chính (Chức năng 2f)
    List<Invoice> findByStatusAndDateBetween(InvoiceStatus status, LocalDate startDate, LocalDate endDate);

    // Tính tổng doanh thu cho báo cáo (Chức năng 2f)
    @Query("SELECT SUM(i.amount) FROM Invoice i WHERE i.status = :status AND i.date BETWEEN :startDate AND :endDate")
    BigDecimal sumRevenueByStatusAndDateRange(InvoiceStatus status, LocalDate startDate, LocalDate endDate);
}
