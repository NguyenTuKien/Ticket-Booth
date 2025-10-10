package com.team7.ticket_booth.controller.web;

import com.team7.ticket_booth.dto.response.MovieResponseDTO;
import com.team7.ticket_booth.dto.response.ShowResponseDTO;
import com.team7.ticket_booth.service.MovieService;
import com.team7.ticket_booth.service.ShowService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class ShowtimeController {

    private final MovieService movieService;
    private final ShowService showService;

    @RequestMapping("/showtime")
    public String Showtime(HttpServletRequest request, @RequestParam UUID movieId) {
        LocalDate date = LocalDate.now().plusDays(1);
        MovieResponseDTO movie = movieService.getById(movieId);
        List<ShowResponseDTO> shows = showService.getShowsByMovieId(movieId);
        // Group shows by date and then by hall
        Map<LocalDate, Map<String, List<ShowResponseDTO>>> groupedShows = showService.getGroupedShowsByMovieId(movieId);
        request.setAttribute("now", date);
        request.setAttribute("movie", movie);
        request.setAttribute("shows", shows);
        request.setAttribute("groupedShows", groupedShows);
        return "showtime"; // => showtime.html
    }


}
