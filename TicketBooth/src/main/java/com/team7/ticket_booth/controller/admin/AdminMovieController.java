package com.team7.ticket_booth.controller.admin;

import com.team7.ticket_booth.dto.request.MovieRequestDTO;
import com.team7.ticket_booth.facade.ReleasingFacade;
import com.team7.ticket_booth.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/movies")
public class AdminMovieController {
    private final MovieService movieService;
    private final ReleasingFacade releasingFacade;

    @PostMapping("")
    public void releaseMovie(@RequestBody MovieRequestDTO dto) {
        releasingFacade.releaseMovie(dto);
    }

    @PutMapping("")
    public void updateMovie(@RequestBody MovieRequestDTO dto, @RequestParam UUID id) {
        movieService.updateMovie(dto, id);
    }

    @DeleteMapping("/refresh")
    public void refreshMovie() {
        movieService.refreshMovies();
    }

    @DeleteMapping("")
    public void deleteMovie(@RequestParam UUID id) {
        movieService.deleteMovie(id);
    }
}
