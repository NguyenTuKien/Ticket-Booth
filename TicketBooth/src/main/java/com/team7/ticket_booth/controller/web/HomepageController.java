package com.team7.ticket_booth.controller.web;

import com.team7.ticket_booth.dto.response.MovieResponseDTO;
import com.team7.ticket_booth.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomepageController {

    private final MovieService movieService;

    @GetMapping({"/", "/home"})
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page) {
        LocalDate now = LocalDate.now().plusDays(1);
        List<MovieResponseDTO> currentlyMovies = movieService.getCurrentMovies(now, page);
        List<MovieResponseDTO> upcomingMovies = movieService.getUpcomingMovies(now, page);
        model.addAttribute("currentlyMovies", currentlyMovies);
        model.addAttribute("upcomingMovies", upcomingMovies);
        return "index"; // => index.html
    }

    @GetMapping("/price")
    public String price() {
        return "price"; // price.html
    }

    @GetMapping("/about")
    public String about() {
        return "about"; // about.html
    }

}