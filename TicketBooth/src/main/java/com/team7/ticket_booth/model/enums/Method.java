package com.team7.ticket_booth.model.enums;

import lombok.Getter;

@Getter
public enum Method  {
    CASH("Tiền mặt"),
    BANK("Chuyển khoản ngân hàng"),
    CARD("Thẻ tín dụng/ghi nợ");

    private final String description;

    Method(String description) {
        this.description = description;
    }

    public static Method findByDescription(String description) {
        for (Method method : values()) {
            if (method.getDescription().equals(description)) {
                return method;
            }
        }
        return null;
    }
}
