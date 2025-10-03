package com.team7.ticket_booth.repository;

import com.team7.ticket_booth.model.Hall;
import com.team7.ticket_booth.model.Seat;
import com.team7.ticket_booth.model.enums.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeatRepository extends JpaRepository<Seat, UUID> {
    List<Seat> findBySeatType(SeatType seatType);

    List<Seat> findByHallId(UUID hallId);

    Optional<Seat> findByPosition(String position);
}
