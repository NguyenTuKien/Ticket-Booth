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

    // Tìm ghế có sẵn cho suất chiếu (ghế chưa có vé được đặt)
    @Query("SELECT s FROM Seat s WHERE s.hall.id = " +
           "(SELECT sh.hall.id FROM Show sh WHERE sh.id = :showId) " +
           "AND s.id NOT IN " +
           "(SELECT t.seat.id FROM Ticket t WHERE t.show.id = :showId AND t.order IS NOT NULL)")
    List<Seat> findAvailableSeatsForShow(@Param("showId") UUID showId);

    // Tìm ghế đã được đặt cho suất chiếu
    @Query("SELECT s FROM Seat s WHERE s.id IN " +
           "(SELECT t.seat.id FROM Ticket t WHERE t.show.id = :showId AND t.order IS NOT NULL)")
    List<Seat> findBookedSeatsForShow(@Param("showId") UUID showId);
}
