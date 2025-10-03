package com.team7.ticket_booth.service;

import com.team7.ticket_booth.dto.request.OrderRequestDTO;
import com.team7.ticket_booth.dto.response.TicketResponseDTO;
import com.team7.ticket_booth.model.Order;
import com.team7.ticket_booth.model.Payment;
import com.team7.ticket_booth.model.Ticket;
import com.team7.ticket_booth.model.User;
import com.team7.ticket_booth.repository.OrderRepository;
import com.team7.ticket_booth.repository.TicketRepository;
import com.team7.ticket_booth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public Order createOrder(OrderRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        LocalDateTime now = LocalDateTime.now();
        return orderRepository.save(new Order(null, user, now, now, new ArrayList<Ticket>(), null));
    }

    public Order updateOrderTicket(Order order, List<Ticket> tickets) {
        order.setTickets(tickets);
        return orderRepository.save(order);
    }

    public Order updateOrderPayment(Order order, Payment payment) {
        order.setPayment(payment);
        return orderRepository.save(order);
    }

    public int getCost(UUID orderId) {
        List<Ticket> tickets = ticketRepository.findByOrderId(orderId);
        List<TicketResponseDTO> ticketResponseDTOS = tickets.stream().map(TicketResponseDTO::new).toList();
        return ticketResponseDTOS.stream().mapToInt(TicketResponseDTO::getPrice).sum();
    }

    public Order getOrderById(UUID orderId) {
        return orderRepository.getById(orderId);
    }

    public void deleteOrder(UUID orderId) {
        orderRepository.deleteById(orderId);
    }
}
