package com.team7.ticket_booth.controller.api;

import com.team7.ticket_booth.dto.response.MovieResponseDTO;
import com.team7.ticket_booth.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/all")
    public List<MovieResponseDTO> getAllMovies(@RequestParam int page) {
        return movieService.getAllMovies(page);
    }

    @GetMapping("/{id}")
    public MovieResponseDTO getById(@PathVariable UUID id){
        return movieService.getById(id);
    }

    @GetMapping("/{title}")
    public MovieResponseDTO getByTitle(@PathVariable String title){
        return movieService.getMovieByTitle(title);
    }

    @GetMapping("/genre/{genre}")
    public List<MovieResponseDTO> getByGenre(@PathVariable String genre, @RequestParam int page){
        return movieService.getMoviesByGenre(genre, page);
    }

    @GetMapping("/current")
    public List<MovieResponseDTO> getCurrentMovies(@RequestParam int page){
        LocalDate now = LocalDate.now();
        return movieService.getCurrentMovies(now, page);
    }

    @GetMapping("/search")
    public List<MovieResponseDTO> searchByKeyword(@RequestParam String keyword, @RequestParam int page){
        return movieService.searchByKeyword(keyword, page);
    }
}

