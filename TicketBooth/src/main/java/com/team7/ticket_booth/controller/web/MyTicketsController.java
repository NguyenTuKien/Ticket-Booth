package com.team7.ticket_booth.controller.web;

import com.team7.ticket_booth.dto.response.TicketResponseDTO;
import com.team7.ticket_booth.model.Order;
import com.team7.ticket_booth.model.Ticket;
import com.team7.ticket_booth.model.User;
import com.team7.ticket_booth.service.user.UserService;
import com.team7.ticket_booth.service.OrderService;
import com.team7.ticket_booth.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MyTicketsController {
    private final TicketService ticketService;
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping("/my-tickets")
    public String getMyTicketsPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login"; // Redirect to login if not authenticated
        }
    // Spring Security often stores an instance of org.springframework.security.core.userdetails.User as principal
    // so we should read the username and fetch our domain User entity instead of casting.
    String username = authentication.getName();
    User user = userService.findByUsername(username);
        List<Order> orders = orderService.getOrdersSucccesByUserId(user.getId());
        List<TicketResponseDTO> tickets = new ArrayList<>();

        for (Order order : orders) {
            List<Ticket> orderTickets = ticketService.getTicketByOrderId(order.getId());
            for (Ticket ticket : orderTickets) {
                tickets.add(new TicketResponseDTO(ticket)); // ✅ sửa new
            }
        }
        model.addAttribute("tickets", tickets);
        return "my-tickets"; // ✅ trả về tên file HTML để Thymeleaf render
    }


}
