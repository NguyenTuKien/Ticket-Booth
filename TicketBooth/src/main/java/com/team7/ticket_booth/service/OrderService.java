package com.team7.ticket_booth.service;

import com.team7.ticket_booth.dto.request.OrderRequestDTO;
import com.team7.ticket_booth.dto.response.OrderResponseDTO;
import com.team7.ticket_booth.dto.response.TicketResponseDTO;
import com.team7.ticket_booth.exception.NotFoundException;
import com.team7.ticket_booth.model.Order;
import com.team7.ticket_booth.model.Payment;
import com.team7.ticket_booth.model.Ticket;
import com.team7.ticket_booth.model.User;
import com.team7.ticket_booth.repository.OrderRepository;
import com.team7.ticket_booth.repository.TicketRepository;
import com.team7.ticket_booth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // Method gốc cho API
    public Order createOrder(OrderRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        LocalDateTime now = LocalDateTime.now();
        return orderRepository.save(new Order(null, user, now, now, new ArrayList<Ticket>(), null));
    }

    // Method mới cho CheckoutController
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        LocalDateTime now = LocalDateTime.now();
        Order order = new Order(null, user, now, now, new ArrayList<>(), null);
        order = orderRepository.save(order);

        return new OrderResponseDTO(order);
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

    // Method gốc trả về entity
    public Order getOrderEntityById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));
    }

    // Method mới trả về DTO cho CheckoutController
    public OrderResponseDTO getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));
        return new OrderResponseDTO(order);
    }

    public void deleteOrder(UUID orderId) {
        orderRepository.deleteById(orderId);
    }

    public List<Order> getOrdersSucccesByUserId(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        return orderRepository.findOrderSuccessByUser(user);
    }

    public List<Ticket> getTicketsByOrderId(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));
        return ticketRepository.findByOrderId(order.getId());
    }
}
