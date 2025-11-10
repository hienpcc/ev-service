package com.ev.billing.controller;

import com.ev.billing.dto.FinancialReportDto;
import com.ev.billing.dto.PaymentRequestDto;
import com.ev.billing.entity.Invoice;
import com.ev.billing.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    // === API cho Khách hàng (Chức năng 1c) ===

    /**
     * [GET] Khách hàng xem lịch sử hóa đơn.
     */
    @GetMapping("/invoices/customer/{customerId}")
    public ResponseEntity<List<Invoice>> getCustomerInvoices(@PathVariable Long customerId) {
        return ResponseEntity.ok(invoiceService.getInvoicesForCustomer(customerId));
    }

    /**
     * [POST] Khách hàng thanh toán một hóa đơn.
     */
    @PostMapping("/invoices/{invoiceId}/pay")
    public ResponseEntity<Invoice> payInvoice(
            @PathVariable Long invoiceId,
            @RequestBody PaymentRequestDto paymentDto) {
        return ResponseEntity.ok(invoiceService.processPayment(invoiceId, paymentDto));
    }

    // === API cho Staff/Admin (Chức năng 2f) ===

    /**
     * [GET] Admin xem báo cáo tài chính.
     */
    @GetMapping("/reports/financial")
    public ResponseEntity<FinancialReportDto> getFinancialReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(invoiceService.getFinancialReport(startDate, endDate));
    }
}
