package com.ev.billing.service;

import com.ev.billing.dto.FinancialReportDto;
import com.ev.billing.dto.PaymentRequestDto;
import com.ev.billing.entity.Invoice;
import com.ev.billing.entity.InvoiceStatus;
import com.ev.billing.kafka.ServiceCompletedEvent;
import com.ev.billing.repository.InvoiceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    // (Bạn cũng có thể inject các service thanh toán online như Stripe, VNPay...)

    /**
     * Tự động tạo hóa đơn khi nhận sự kiện từ Kafka.
     */
    @Transactional
    public void createInvoiceFromEvent(ServiceCompletedEvent event) {
        Invoice invoice = new Invoice();
        invoice.setRecordId(event.getRecordId());
        invoice.setCustomerId(event.getCustomerId());
        invoice.setAmount(event.getTotalCost());
        // (Status và Date được set tự động bởi @PrePersist)

        invoiceRepository.save(invoice);

        // (Sau khi lưu, có thể gửi event 'invoice_created'
        // để NOTIFICATION-SERVICE thông báo cho khách hàng)
    }

    /**
     * Xử lý thanh toán (Chức năng 1c).
     */
    @Transactional
    public Invoice processPayment(Long invoiceId, PaymentRequestDto paymentDto) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new IllegalStateException("Invoice is already paid.");
        }

        // 1. Gọi API của cổng thanh toán (Stripe, VNPay, ...)
        // boolean paymentSuccess = paymentGateway.charge(invoice.getAmount(), paymentDto.getCardToken());

        // 2. Nếu thanh toán thành công:
        // (Giả sử thanh toán luôn thành công)
        boolean paymentSuccess = true;
        if (paymentSuccess) {
            invoice.setStatus(InvoiceStatus.PAID);
            invoice.setPaymentMethod(paymentDto.getPaymentMethod());
            return invoiceRepository.save(invoice);
        } else {
            throw new RuntimeException("Payment failed.");
        }
    }

    /**
     * Lấy hóa đơn cho khách hàng.
     */
    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesForCustomer(Long customerId) {
        return invoiceRepository.findByCustomerIdOrderByDateDesc(customerId);
    }

    /**
     * Tạo báo cáo tài chính (Chức năng 2f).
     */
    @Transactional(readOnly = true)
    public FinancialReportDto getFinancialReport(LocalDate startDate, LocalDate endDate) {
        // Tính tổng doanh thu đã thu (PAID)
        BigDecimal totalRevenue = invoiceRepository.sumRevenueByStatusAndDateRange(
                InvoiceStatus.PAID, startDate, endDate
        );

        // Lấy danh sách hóa đơn chưa thanh toán
        List<Invoice> unpaidInvoices = invoiceRepository.findByStatusAndDateBetween(
                InvoiceStatus.UNPAID, startDate, endDate
        );

        // Tính tổng nợ
        BigDecimal totalOutstanding = unpaidInvoices.stream()
                .map(Invoice::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        FinancialReportDto report = new FinancialReportDto();
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        report.setTotalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO);
        report.setTotalOutstanding(totalOutstanding);
        report.setUnpaidInvoiceCount(unpaidInvoices.size());

        // (Logic tính "chi phí" và "lợi nhuận" sẽ phức tạp hơn, 
        // cần dữ liệu chi phí nhập phụ tùng...)

        return report;
    }
}