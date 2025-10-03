package com.team7.ticket_booth.controller;

import com.team7.ticket_booth.dto.response.TicketResponseDTO;
import com.team7.ticket_booth.service.TicketService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @GetMapping("")
    public List<TicketResponseDTO> getTicketsInShow(@RequestParam UUID showId) {
        return ticketService.getUnorderedTicketsByShowId(showId);
    }

}
