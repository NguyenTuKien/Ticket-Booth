package com.team7.ticket_booth.dto.response;

import com.team7.ticket_booth.model.Payment;
import com.team7.ticket_booth.model.enums.Method;
import com.team7.ticket_booth.model.enums.Status;
import lombok.Data;

import java.util.UUID;

@Data
public class PaymentResponseDTO {
    private UUID id;
    private String orderCode;
    private String method;
    private String status;
    private String provider;
    private String url;
    private int cost;

    public PaymentResponseDTO(Payment payment, int cost) {
        this.id = payment.getId();
        this.orderCode = String.valueOf(payment.getOrder().getId());
        this.method = payment.getMethod().getDescription();
        this.status = payment.getStatus().getDescription();
        this.provider = payment.getProviderTxnId();
        this.url = payment.getPaymentUrl();
        this.cost = cost;
    }
}
