package com.team7.ticket_booth.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.team7.ticket_booth.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {
    @Query("SELECT c FROM Card c WHERE c.user.id = :id")
    Optional<Card> findByUserId(UUID id);
}
