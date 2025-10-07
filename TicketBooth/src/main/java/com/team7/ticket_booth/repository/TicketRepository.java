package com.team7.ticket_booth.repository;

import com.team7.ticket_booth.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    List<Ticket> findByOrderId(UUID orderId);

    @Query("SELECT t FROM Ticket t WHERE t.show.id = :showId")
    List<Ticket> findByShowId(@Param("showId") UUID showId);

    // TicketRepository.java
    @Query("""
    SELECT t FROM Ticket t
    LEFT JOIN t.order o
    LEFT JOIN o.payment p
    WHERE t.show.id = :showId
      AND (o IS NULL OR p.status != 'SUCCESS')
    ORDER BY t.seat.position ASC
    """)
    List<Ticket> findAvailableTickets(@Param("showId") UUID showId);

}
