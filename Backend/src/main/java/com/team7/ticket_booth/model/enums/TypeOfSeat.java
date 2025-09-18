package com.team7.ticket_booth.model.enums;

import lombok.Getter;

@Getter
public enum TypeOfSeat {
    NORMAL ("Ghế thường"),
    VIP ("Ghế VIP"),
    COUPLE  ("Ghế đôi");

    private final String description;

    TypeOfSeat(String description) {
        this.description = description;
    }
}
