package com.team7.ticket_booth.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class TicketRequestDTO {
    private UUID showId;
    private UUID seatId;
}
