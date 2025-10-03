package com.team7.ticket_booth.dto.request;

import com.team7.ticket_booth.model.Hall;
import com.team7.ticket_booth.model.enums.SeatType;
import lombok.Data;

@Data
public class SeatRequestDTO {
    private char row;
    private int column;
    private Long hallId;
    private SeatType seatType;
}
