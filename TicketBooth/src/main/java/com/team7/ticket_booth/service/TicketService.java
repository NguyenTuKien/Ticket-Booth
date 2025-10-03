package com.team7.ticket_booth.service;

import com.team7.ticket_booth.dto.request.TicketRequestDTO;
import com.team7.ticket_booth.exception.NotFoundException;
import com.team7.ticket_booth.exception.RequestException;
import com.team7.ticket_booth.model.*;
import com.team7.ticket_booth.model.enums.SeatType;
import com.team7.ticket_booth.model.enums.Shift;
import com.team7.ticket_booth.repository.*;
import com.team7.ticket_booth.dto.response.TicketResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;
    private final PriceRepository priceRepository;
    private final OrderRepository orderRepository;

    public void createTicket(TicketRequestDTO dto) {
        Show show = showRepository.findById(dto.getShowId()).
                orElseThrow(() -> new RuntimeException("Show not found with id: " + dto.getShowId()));
        Hall hall = show.getHall();
        List<Seat> seats = seatRepository.findByHallId(hall.getId());
        for (Seat seat : seats){
            Shift shift = show.getShift();
            SeatType seatType = seat.getSeatType();
            Price price = priceRepository.findByShiftAndSeatType(shift, seatType).
                    orElseThrow(() -> new RuntimeException("Price not found for shift: " + shift + " and seat type: " + seatType));
            int value = price.getValue();
            Ticket ticket = new Ticket(null, null, show, seat, value);
            ticketRepository.save(ticket);
        }
    }

    public Ticket getTicketById(UUID id) {
        return ticketRepository.getById(id);
    }

    public Ticket updateTicket(UUID ticketId, UUID orderId) {
        Ticket ticket = ticketRepository.getById(ticketId);
        Order order = orderRepository.getById(orderId);
        ticket.setOrder(order);
        return ticketRepository.save(ticket);
    }

    public Ticket resetTicket(UUID uuid) {
        Ticket ticket = ticketRepository.getById(uuid);
        ticket.setOrder(null);
        return ticketRepository.save(ticket);
    }

    public List<TicketResponseDTO> getUnorderedTicketsByShowId(UUID id){
        if(!showRepository.existsById(id)) throw new NotFoundException("Show not found with id: " + id);
        List<Ticket> tickets = ticketRepository.findUnorderedTicketsByShowId(id);
        if(tickets.isEmpty()) throw new RequestException("No unordered tickets found for show id: " + id);
        return tickets.stream().map(TicketResponseDTO::new).toList();
    }
}