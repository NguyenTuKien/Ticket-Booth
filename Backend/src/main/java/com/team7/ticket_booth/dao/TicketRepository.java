package com.team7.ticket_booth.dao;

import com.team7.ticket_booth.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByShow_Id(Long showId);

    List<Ticket> findByBooking_Id(Long bookingId);

    Optional<Ticket> findBySeat_IdAndShow_Id(Long seatId, Long showId);

    List<Ticket> findByShow_IdAndPrice(Long showId, Integer price);

    Integer countByBooking_Id(Long bookingId);

    @Query("SELECT SUM(t.price) FROM Ticket t WHERE t.booking.id = ?1")
    Integer sumPriceByBooking_Id(Long bookingId);

    Integer countByShow_Id(Long showId);
}
