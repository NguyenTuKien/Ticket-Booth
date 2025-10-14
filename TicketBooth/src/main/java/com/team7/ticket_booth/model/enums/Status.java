package com.team7.ticket_booth.model.enums;

import lombok.Getter;

@Getter
public enum Status {
    PENDING ("Đang chờ"),
    PAID ("Đã thanh toán");

    private final String description;

    Status(String description) {
        this.description = description;
    }
}
