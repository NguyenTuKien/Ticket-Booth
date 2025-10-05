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
    private Double price;

    public SeatResponseDTO(Seat seat) {
        this.id = seat.getId();
        this.position = seat.getPosition();
        this.seatType = seat.getSeatType();
        // Tạm thời set price mặc định, có thể cần tính toán từ Price entity
        this.price = 100000.0; // Giá mặc định
    }
}
