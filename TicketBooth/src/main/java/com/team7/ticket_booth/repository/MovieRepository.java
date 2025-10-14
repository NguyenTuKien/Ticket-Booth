package com.team7.ticket_booth.repository;

import com.team7.ticket_booth.model.Movie;
import com.team7.ticket_booth.model.enums.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {

    Optional<Movie> findByTitle(String title);

    Page<Movie> findByGenre(Genre genre, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE :currentDate BETWEEN m.beginDay AND m.endDay")
    Page<Movie> findCurrentlyShowingMovies(@Param("currentDate") LocalDate currentDate, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE :currentDate < m.beginDay")
    Page<Movie> findComingShowingMovies(@Param("currentDate") LocalDate currentDate, Pageable pageable);
            
    @Query("SELECT m FROM Movie m WHERE :currentDate > m.endDay")
    List<Movie> findMoviesEndedBefore(@Param("currentDate") LocalDate currentDate);

    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT m FROM Movie m ORDER BY m.rating DESC")
    Page<Movie> findMovieOrderByRatingDesc(Pageable pageable);
}
