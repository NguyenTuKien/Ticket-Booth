package com.team7.ticket_booth.model.enums;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN ("Quản trị viên"),
    GUEST("Khách hàng");

    private final String description;

    Role(String description) {
        this.description = description;
    }
}
