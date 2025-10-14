package com.team7.ticket_booth.dto.response;

import com.team7.ticket_booth.model.Movie;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class MovieResponseDTO {
    private UUID id;
    private String title;
    private String genre;
    private String thumbnailUrl;
    private String trailerUrl;
    private String description;
    private int duration;
    private float rating;
    private LocalDate beginDay;
    private LocalDate endDay;

    public MovieResponseDTO(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.genre = (movie.getGenre() != null) ? movie.getGenre().getDescription() : null;
        this.thumbnailUrl = movie.getThumbnailUrl();
        this.trailerUrl = movie.getTrailerUrl();
        this.description = movie.getDescription();
        this.rating = movie.getRating();
        this.duration = movie.getDuration();
        this.beginDay = movie.getBeginDay();
        this.endDay = movie.getEndDay();
    }
}
