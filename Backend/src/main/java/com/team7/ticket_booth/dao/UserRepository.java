package com.team7.ticket_booth.dao;

import com.team7.ticket_booth.model.User;
import com.team7.ticket_booth.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsernameOrEmail(String username, String email);

    List<User> findByRole(Role role);

    List<User> findByFullNameContainingIgnoreCase(String keyword);

    @Query("SELECT u FROM User u JOIN u.bookings b GROUP BY u ORDER BY COUNT(b) DESC LIMIT 10")
    List<User> findTopUsers();

    @Query("SELECT DISTINCT u FROM User u JOIN u.bookings b WHERE b.createdAt BETWEEN :start AND :end")
    List<User> findUsersWithBookingsBetween(LocalDateTime start, LocalDateTime end);

}
