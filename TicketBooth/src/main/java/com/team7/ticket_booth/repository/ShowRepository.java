package com.team7.ticket_booth.repository;

import com.team7.ticket_booth.model.Hall;
import com.team7.ticket_booth.model.Movie;
import com.team7.ticket_booth.model.Show;
import com.team7.ticket_booth.model.enums.Shift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShowRepository extends JpaRepository <Show, UUID>{
    @Query("SELECT s FROM Show s ORDER BY s.showDate ASC, s.shift ASC")
    Page<Show> findAllByShowDateAndShiftAsc(Pageable pageable);

    @Query("SELECT s FROM Show s WHERE s.movie.id = :id ORDER BY s.showDate ASC, s.shift ASC")
    List<Show> findByMovieIdOrderByShowDateAscShiftAsc(UUID id);

}
