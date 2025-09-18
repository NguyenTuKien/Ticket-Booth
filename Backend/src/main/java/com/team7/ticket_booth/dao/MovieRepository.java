//package com.team7.ticket_booth.dao;
//
//import com.team7.ticket_booth.model.Movie;
//import com.team7.ticket_booth.model.enums.Genre;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface MovieRepository extends JpaRepository<Movie, Integer> {
//
//    Optional<Movie> findByTitle(String title);
//
//    List<Movie> findByGenre(Genre genre);
//
//    List<Movie> findByBeginDayLessThanEqualAndEndDayGreaterThanEqual(LocalDate date1, LocalDate date2);
//
//    @Query("SELECT m FROM Movie m WHERE :currentDate BETWEEN m.beginDay AND m.endDay")
//    List<Movie> findCurrentlyShowingMovies(@Param("currentDate") LocalDate currentDate);
//
//    List<Movie> findByBeginDayAfter(LocalDate date);
//
//    List<Movie> findByDurationBetween(int minDuration, int maxDuration);
//
//    List<Movie> findByTitleContainingIgnoreCase(String keyword);
//
//    @Query("SELECT m FROM Movie m WHERE m.genre = :genre AND :currentDate BETWEEN m.beginDay AND m.endDay")
//    List<Movie> findByGenreAndCurrentlyShowing(@Param("genre") Genre genre, @Param("currentDate") LocalDate currentDate);
//
//    List<Movie> findByDurationGreaterThanEqual(int duration);
//
//    List<Movie> findByDurationLessThanEqual(int duration);
//
//    long countByGenre(Genre genre);
//
//    boolean existsByTitle(String title);
//}
