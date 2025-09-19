package com.team7.ticket_booth.dao;

import com.team7.ticket_booth.model.Booking;
import com.team7.ticket_booth.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Booking doesn't have a ticketId field; use a join on Ticket to fetch Booking by Ticket id
    @Query("SELECT b FROM Booking b JOIN b.tickets t WHERE t.id = :ticketId")
    Optional<Booking> findByTicketId(@Param("ticketId") Long ticketId);

    // Correct property path to nested user id
    Optional<Booking> findByUser_Id(Long userId);

    // Return the enum type and fetch status via join on Ticket
    @Query("SELECT b.status FROM Booking b JOIN b.tickets t WHERE t.id = :ticketId")
    Status findStatusByTicketId(@Param("ticketId") Long ticketId);
}
