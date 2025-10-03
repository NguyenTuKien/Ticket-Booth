package com.team7.ticket_booth.repository;

import com.team7.ticket_booth.model.Hall;
import com.team7.ticket_booth.model.enums.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HallRepository extends JpaRepository<Hall, UUID> {
    Optional<Hall> findByName(String name);

    @Query("SELECT h.id FROM Hall h WHERE h.name = :name")
    Optional<UUID> findHallIdByName(String name);

    @Query("""
    SELECT h FROM Hall h
    WHERE NOT EXISTS (
        SELECT s FROM Show s
        WHERE s.hall = h
          AND s.shift = :shift
          AND s.showDate = :showDate
    )
""")
    List<Hall> findAllAvailableHalls(Shift shift, LocalDate showDate);
}
