package com.team7.ticket_booth.model.enums;

import lombok.Getter;

@Getter
public enum Status {
    PENDING ("Đang chờ"),
    SUCCESS ("Thành công"),
    FAILED ("Thất bại"),
    REFUNDED ("Đã hoàn tiền"),
    CANCELED ("Đã bị hủy");

    private final String description;

    Status(String description) {
        this.description = description;
    }
}
