package com.team7.ticket_booth.dao;

import com.team7.ticket_booth.model.Show;
import com.team7.ticket_booth.model.enums.Genre;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository <Show, Long>{
    List<Show> findByMovie_Title(String movieTitle, Pageable pageable);

    List<Show> findByMovie_Genre(Genre movieGenre, Pageable pageable);

    List<Show> findByHall_Id(Long hallId, Pageable pageable);

    List <Show> findByMovie_Id(Long movieId, Pageable pageable);

    List<Show> findByDay(LocalDate day, Pageable pageable);

    List<Show> findByShift(LocalDateTime shift, Pageable pageable);
}
