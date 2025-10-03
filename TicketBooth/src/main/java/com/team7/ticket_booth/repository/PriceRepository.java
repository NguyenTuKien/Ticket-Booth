package com.team7.ticket_booth.repository;

import com.team7.ticket_booth.model.Price;
import com.team7.ticket_booth.model.enums.SeatType;
import com.team7.ticket_booth.model.enums.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PriceRepository extends JpaRepository<Price, UUID> {
    Optional<Price> findByShiftAndSeatType(Shift shift, SeatType seatType);

    Optional<Price> findByShift(Shift shift);

    Optional<Price> findBySeatType(SeatType seatType);
}
