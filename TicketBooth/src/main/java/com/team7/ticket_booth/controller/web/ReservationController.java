package com.team7.ticket_booth.controller.web;

import com.team7.ticket_booth.dto.response.MovieResponseDTO;
import com.team7.ticket_booth.dto.response.ShowResponseDTO;
import com.team7.ticket_booth.dto.response.TicketResponseDTO;
import com.team7.ticket_booth.model.User;
import com.team7.ticket_booth.service.ShowService;
import com.team7.ticket_booth.service.TicketService;
import com.team7.ticket_booth.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ReservationController {
    private final ShowService showService;
    private final TicketService ticketService;
    private final UserService userService;

    @RequestMapping("/reservation")
    public String show(HttpServletRequest request, @RequestParam UUID showId) {
        ShowResponseDTO show = showService.getShowById(showId);
        MovieResponseDTO movie = showService.getMovieByShowId(showId);
        List<TicketResponseDTO> allTickets = ticketService.getTicketByShowId(showId);
        List<TicketResponseDTO> availableTickets = ticketService.getUnorderedTicketsByShowId(showId);

        // Get authenticated user ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = null;  
        String email = null;
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            // Query database to get User entity
            User user = userService.findByUsername(username);
            if (user != null) {
                userId = user.getId();
            }
        }
        request.setAttribute("show", show);
        request.setAttribute("movie", movie);
        request.setAttribute("tickets", allTickets);
        request.setAttribute("availableTickets", availableTickets);
        request.setAttribute("userId", userId);
        return "reservation"; // => reservation.html
    }
}
