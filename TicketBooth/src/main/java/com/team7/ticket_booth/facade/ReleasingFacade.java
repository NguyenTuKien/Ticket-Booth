package com.team7.ticket_booth.facade;

import com.team7.ticket_booth.dto.request.MovieRequestDTO;
import com.team7.ticket_booth.dto.request.ShowRequestDTO;
import com.team7.ticket_booth.dto.request.TicketRequestDTO;
import com.team7.ticket_booth.model.Show;
import com.team7.ticket_booth.service.HallService;
import com.team7.ticket_booth.service.MovieService;
import com.team7.ticket_booth.service.ShowService;
import com.team7.ticket_booth.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReleasingFacade {

    private final MovieService movieService;
    private final ShowService showService;
    private final TicketService ticketService;

    @Transactional
    public void releaseMovie(MovieRequestDTO dto) {
        movieService.createMovie(dto);
        ShowRequestDTO showRequestDTO = new ShowRequestDTO();
        showRequestDTO.setMovieTitle(dto.getTitle());
        showRequestDTO.setStartTimes(dto.getStartTimes());
        List <Show> shows = showService.createShow(showRequestDTO);
        for (Show show : shows){
            TicketRequestDTO ticketRequestDTO = new TicketRequestDTO();
            ticketRequestDTO.setShowId(show.getId());
            ticketService.createTicket(ticketRequestDTO);
        }
    }
}
