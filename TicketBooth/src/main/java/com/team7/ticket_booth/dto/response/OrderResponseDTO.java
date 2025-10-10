package com.team7.ticket_booth.dto.response;

import com.team7.ticket_booth.model.Order;
import com.team7.ticket_booth.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private UUID id;
    private String userName;
    private String userEmail;
    private LocalDateTime orderDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double totalAmount;
    private Status paymentStatus;
    private String paymentMethod;
    private List<TicketResponseDTO> tickets;
    private PaymentResponseDTO payment;

    public OrderResponseDTO(Order order) {
        this.id = order.getId();
        this.userName = order.getUser().getUsername();
        this.userEmail = order.getUser().getEmail();
        this.orderDate = order.getCreatedAt();
        this.createdAt = order.getCreatedAt();
        this.updatedAt = order.getUpdatedAt();
    }
}
