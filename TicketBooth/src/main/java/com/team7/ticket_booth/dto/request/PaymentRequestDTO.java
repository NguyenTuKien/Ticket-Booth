package com.team7.ticket_booth.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentRequestDTO {
    private UUID orderId;
    private int cost;
    private String method;
}
