package com.team7.ticket_booth.model.enums;

import lombok.Getter;

@Getter
public enum SeatType {
    NORMAL ("Ghế thường"),
    VIP ("Ghế VIP"),
    COUPLE  ("Ghế đôi");

    private final String description;

    SeatType(String description) {
        this.description = description;
    }

    public static SeatType findByDescription(String description) {
        for (SeatType seatType : values()) {
            if (seatType.getDescription().equals(description)) {
                return seatType;
            }
        }
        return null;
    }
}
