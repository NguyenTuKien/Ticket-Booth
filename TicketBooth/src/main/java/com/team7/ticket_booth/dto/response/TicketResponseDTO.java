package com.team7.ticket_booth.dto.response;

import com.team7.ticket_booth.model.Ticket;
import com.team7.ticket_booth.model.enums.Shift;
import com.team7.ticket_booth.model.enums.SeatType;
import lombok.Data;

import java.util.UUID;

@Data
public class TicketResponseDTO {
    private UUID id;
    private int price;
    private String movieTitle;
    private String thumbnailUrl;
    private String position;
    private Shift shift;
    private String hallName;
    private UUID orderCode;
    private SeatType seatType;
    private String showDate; // ISO date string
    private String startTime; // derived from shift

    public TicketResponseDTO(Ticket ticket){
        this.id = ticket.getId();
        this.price = ticket.getPrice();
        this.movieTitle = ticket.getShow().getMovie().getTitle();
        this.thumbnailUrl = ticket.getShow().getMovie().getThumbnailUrl();
        this.position = ticket.getSeat().getPosition();
        this.shift = Shift.valueOf(ticket.getShow().getShift().toString());
        this.hallName = ticket.getShow().getHall().getName();
        this.orderCode = (ticket.getOrder() != null) ? ticket.getOrder().getId() : null;
        this.seatType = ticket.getSeat().getSeatType();
        this.showDate = ticket.getShow().getShowDate().toString();
        this.startTime = ticket.getShow().getShift() != null ? ticket.getShow().getShift().getStartTime().toString() : null;
    }
}
