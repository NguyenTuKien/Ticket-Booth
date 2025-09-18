package com.team7.ticket_booth.model.enums;

import lombok.Getter;

@Getter
public enum Method  {
    CASH("Tiền mặt"),
    BANK_TRANSFER("Chuyển khoản ngân hàng");

    private final String description;

    Method(String description) {
        this.description = description;
    }
}
