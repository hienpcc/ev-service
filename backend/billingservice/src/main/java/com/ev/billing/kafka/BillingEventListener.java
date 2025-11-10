package com.ev.billing.kafka;

import com.ev.billing.service.InvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j // Dùng để log
public class BillingEventListener {

    private final InvoiceService invoiceService;
    private final ObjectMapper objectMapper; // Spring Boot tự động cung cấp

    // "service_completed" là tên Topic mà Maintenance-Service publish
    @KafkaListener(topics = "service_completed", groupId = "billing_group")
    public void handleServiceCompleted(String message) {
        log.info("Received event from Kafka: {}", message);
        try {
            // 1. Chuyển đổi message (String) sang Object
            ServiceCompletedEvent event = objectMapper.readValue(message, ServiceCompletedEvent.class);

            // 2. Gọi service để tạo hóa đơn
            invoiceService.createInvoiceFromEvent(event);

        } catch (Exception e) {
            log.error("Failed to process Kafka event: {}", message, e);
            // (Cần có cơ chế retry hoặc đưa vào Dead Letter Queue)
        }
    }
}