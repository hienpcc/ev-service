package com.ev.billing.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class FinancialReportDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalRevenue; // Tổng doanh thu
    private BigDecimal totalOutstanding; // Tổng nợ (chưa trả)
    private int unpaidInvoiceCount;
}