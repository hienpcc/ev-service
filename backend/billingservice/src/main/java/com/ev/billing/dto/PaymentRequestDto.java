package com.ev.billing.dto;
import lombok.Data;
@Data
public class PaymentRequestDto {
    private String paymentMethod; // "ONLINE_BANKING", "CREDIT_CARD", "E_WALLET"
    private String cardToken; // Token từ cổng thanh toán
}