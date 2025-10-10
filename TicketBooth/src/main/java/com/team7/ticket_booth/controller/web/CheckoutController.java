package com.team7.ticket_booth.controller.web;

import com.team7.ticket_booth.dto.response.PaymentResponseDTO;
import com.team7.ticket_booth.model.Card;
import com.team7.ticket_booth.model.Order;
import com.team7.ticket_booth.model.Ticket;
import com.team7.ticket_booth.model.User;
import com.team7.ticket_booth.service.OrderService;
import com.team7.ticket_booth.repository.CardRepository;
import com.team7.ticket_booth.repository.PaymentRepository;
import com.team7.ticket_booth.service.TicketService;
import com.team7.ticket_booth.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class CheckoutController {
    private final OrderService orderService;
    private final TicketService ticketService;
    private final UserService userService;
    private final PaymentRepository paymentRepository;
    private final CardRepository cardRepository;

    // GET hiển thị trang chi tiết đơn hàng
    @GetMapping("/checkout")
    public String viewOrder(@RequestParam("orderId") UUID orderId, Model model) {
        Order order = orderService.getOrderEntityById(orderId);
        List<Ticket> tickets = ticketService.getTicketByOrderId(order.getId());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null;
        Card userCard = null;
        String email = null;
        if (authentication != null && authentication.isAuthenticated()) {
            try {
                currentUser = userService.findByUsername(authentication.getName());
                email = currentUser.getEmail();
                userCard = cardRepository.findByUserId(currentUser.getId()).orElse(null);
            } catch (Exception ignored) {}
        }
        int totalCost = orderService.getCost(order.getId());
        var paymentEntity = paymentRepository.findByOrderId(order.getId())
                .orElseThrow(() -> new RuntimeException("Payment not found for order " + orderId));
        PaymentResponseDTO payment = new PaymentResponseDTO(paymentEntity, totalCost);
        model.addAttribute("tickets", tickets);
        model.addAttribute("order", order);
        model.addAttribute("payment", payment);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userCard", userCard);
        model.addAttribute("email", email);
        return "checkout";
    }
}
