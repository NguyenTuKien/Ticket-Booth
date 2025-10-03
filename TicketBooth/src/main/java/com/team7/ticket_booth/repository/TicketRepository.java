package com.team7.ticket_booth.repository;

import com.team7.ticket_booth.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    List<Ticket> findByOrderId(UUID orderId);

    @Query("SELECT t FROM Ticket t WHERE t.order IS NULL AND t.show.id = :showId ORDER BY t.seat.position ASC ")
    List<Ticket> findUnorderedTicketsByShowId(UUID showId);

}
