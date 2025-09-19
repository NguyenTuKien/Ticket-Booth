package com.team7.ticket_booth.dao;

import com.team7.ticket_booth.model.Seat;
import com.team7.ticket_booth.model.enums.TypeOfSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByTypeOfSeat(TypeOfSeat typeOfSeat);
}
