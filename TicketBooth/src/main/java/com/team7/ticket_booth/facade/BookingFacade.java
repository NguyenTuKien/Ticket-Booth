package com.team7.ticket_booth.facade;

import com.team7.ticket_booth.dto.request.OrderRequestDTO;
import com.team7.ticket_booth.dto.request.PaymentRequestDTO;
import com.team7.ticket_booth.dto.response.OrderResponseDTO;
import com.team7.ticket_booth.dto.response.PaymentResponseDTO;
import com.team7.ticket_booth.dto.response.ShowResponseDTO;
import com.team7.ticket_booth.model.Order;
import com.team7.ticket_booth.model.Payment;
import com.team7.ticket_booth.model.Show;
import com.team7.ticket_booth.model.User;
import com.team7.ticket_booth.model.enums.Status;
import com.team7.ticket_booth.service.*;
import com.team7.ticket_booth.service.user.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookingFacade {
    private final OrderService orderService;
    private final ShowService showService;
    private final TicketService ticketService;
    private final AuthService authService;
    private final PaymentService paymentService;

    @Transactional
    public PaymentResponseDTO orderTickets(OrderRequestDTO dto) {
        ShowResponseDTO show = showService.getShowById(dto.getShowId());
        User user = authService.getUserById(dto.getUserId());
        Order order = orderService.createOrder(dto);
        for (UUID ticketId : dto.getTicketIds()) {
            ticketService.updateTicket(ticketId, order.getId());
        }
        int totalCost = orderService.getCost(order.getId());
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setOrderId(order.getId());
        paymentRequestDTO.setCost(totalCost);
        paymentRequestDTO.setMethod(dto.getMethod());
        Payment payment = paymentService.createPayment(paymentRequestDTO);
        return new PaymentResponseDTO(payment, totalCost);
    }

    @Transactional
    public void checkOrder(UUID orderId) {
        Payment payment = paymentService.getByOdrerId(orderId);
        if(payment.getStatus().equals(Status.PENDING)){
            orderService.deleteOrder(orderId);
        }
    }


    public void cancelOrder(UUID orderId) {
        OrderResponseDTO order = orderService.getOrderById(orderId);
        for (var ticket : order.getTickets()) {
            ticketService.resetTicket(ticket.getId());
        }
        orderService.deleteOrder(orderId);
    }

    public void cancelTicket(List<UUID> ticketIds){
        for (UUID ticketId : ticketIds) {
            ticketService.resetTicket(ticketId);
        }
    }
}
