package com.team7.ticket_booth.model.enums;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public enum Shift {
    MORNING(LocalTime.of(8, 0), LocalTime.of(10, 15)),
    LATE_MORNING(LocalTime.of(10, 30), LocalTime.of(12, 45)),
    AFTERNOON(LocalTime.of(13, 0), LocalTime.of(15, 15)),
    LATE_AFTERNOON(LocalTime.of(15, 30), LocalTime.of(17, 45)),
    EVENING(LocalTime.of(18, 0), LocalTime.of(20, 15)),
    LATE_EVENING(LocalTime.of(20, 30), LocalTime.of(22, 45)),
    MIDNIGHT(LocalTime.of(23, 0), LocalTime.of(1, 15)); // qua ngày hôm sau

    private final LocalTime startTime;
    private final LocalTime endTime;

    Shift(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
