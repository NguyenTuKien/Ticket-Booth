package com.team7.ticket_booth.controller.api;

import com.team7.ticket_booth.dto.request.OrderRequestDTO;
import com.team7.ticket_booth.dto.response.PaymentResponseDTO;
import com.team7.ticket_booth.facade.BookingFacade;
import com.team7.ticket_booth.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final BookingFacade bookingFacade;

    @PostMapping("/booking")
    public PaymentResponseDTO orderTickets(@RequestBody OrderRequestDTO dto) {
        return bookingFacade.orderTickets(dto);
    }

    @DeleteMapping("/order")
    public void cancelOrder(@RequestParam UUID orderId) {
        bookingFacade.cancelOrder(orderId);
    }

    @DeleteMapping("/tickets")
    public void cancelTickets(@RequestBody List<UUID> ticketIds) {
        bookingFacade.cancelTicket(ticketIds);
    }
}
