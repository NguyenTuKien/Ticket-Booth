package com.team7.ticket_booth.dto.response;

import com.team7.ticket_booth.model.Seat;
import com.team7.ticket_booth.model.enums.SeatType;
import lombok.Data;

import java.util.UUID;

@Data
public class SeatResponseDTO {
    private UUID id;
    private String position;
    private SeatType seatType;

    public SeatResponseDTO(Seat seat) {
        this.id = seat.getId();
        this.position = seat.getPosition();
        this.seatType = seat.getSeatType();
    }
}
