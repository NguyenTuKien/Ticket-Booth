package com.team7.ticket_booth.service;

import com.team7.ticket_booth.dto.request.MovieRequestDTO;
import com.team7.ticket_booth.dto.response.MovieResponseDTO;
import com.team7.ticket_booth.dto.response.ShowResponseDTO;
import com.team7.ticket_booth.exception.NotFoundException;
import com.team7.ticket_booth.exception.RequestException;
import com.team7.ticket_booth.model.Movie;
import com.team7.ticket_booth.model.enums.Genre;
import com.team7.ticket_booth.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public void createMovie(MovieRequestDTO dto) {
        try {
            Movie movie = new Movie().builder().
                    title(dto.getTitle()).
                    genre(Genre.findByDescription(dto.getGenre())).
                    description(dto.getDescription()).
                    thumbnailUrl(dto.getThumbnailUrl()).
                    duration(dto.getDuration()).
                    beginDay(dto.getBeginDay()).
                    endDay(dto.getEndDay()).
                    build();

            movieRepository.save(movie);
        } catch (Exception e) {
            throw new RuntimeException("Invalid movie create request");
        }
    }

    public MovieResponseDTO getById(UUID id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found"));
        return new MovieResponseDTO(movie);
    }

    public List<MovieResponseDTO> getAllMovies(int page) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<Movie> movies = movieRepository.findAll(pageable);
        return movies.stream().map(MovieResponseDTO::new).toList();
    }

    public List<MovieResponseDTO> getMoviesByGenre(String genreStr, int page) {
        Genre genre = Genre.findByDescription(genreStr);
        if (genre == null) {
            throw new RequestException("Invalid genre");
        }
        Pageable pageable = PageRequest.of(page, 20);
        Page<Movie> movies = movieRepository.findByGenre(genre, pageable);
        return movies.getContent().stream().map(MovieResponseDTO::new).toList();
    }

    public List<MovieResponseDTO> getCurrentMovies(int page) {
        LocalDate currentDate = LocalDate.now();
        Pageable pageable = PageRequest.of(page, 20);
        Page<Movie> movies = movieRepository.findCurrentlyShowingMovies(currentDate, pageable);
        return movies.getContent().stream().map(MovieResponseDTO::new).toList();
    }

    public List<MovieResponseDTO> getUpcomingMovies(int page) {
        LocalDate currentDate = LocalDate.now();
        Pageable pageable = PageRequest.of(page, 20);
        Page<Movie> movies = movieRepository.findComingShowingMovies(currentDate, pageable);
        return movies.getContent().stream().map(MovieResponseDTO::new).toList();
    }

    public MovieResponseDTO getMovieByTitle(String title) {
        Movie movie = movieRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        return new MovieResponseDTO(movie);
    }

    public List<MovieResponseDTO> searchByKeyword(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<Movie> movies = movieRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        if (movies.isEmpty()) {
            throw new RequestException("No movies found with the given keyword");
        }
        return movies.getContent().stream().map(MovieResponseDTO::new).toList();
    }


    public MovieResponseDTO updateMovie(MovieRequestDTO dto, UUID id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RequestException("Movie not found"));
        movie.setTitle(dto.getTitle());
        movie.setGenre(Genre.findByDescription(dto.getGenre()));
        movie.setDescription(dto.getDescription());
        movie.setThumbnailUrl(dto.getThumbnailUrl());
        movie.setDuration(dto.getDuration());
        movie.setBeginDay(dto.getBeginDay());
        movie.setEndDay(dto.getEndDay());

        return new MovieResponseDTO(movieRepository.save(movie));
    }

    public void deleteMovie(UUID id) {
        if(!movieRepository.existsById(id)) throw new NotFoundException("Movie not found");
        movieRepository.deleteById(id);
    }
    @Transactional
    public void refreshMovies(){
        LocalDate currentDate = LocalDate.now();
        List<Movie> movies = movieRepository.findMoviesEndedBefore(currentDate);
        if(movies.isEmpty()) return;
        movieRepository.deleteAll(movies);
    }

}
