package com.team7.ticket_booth.dao;

import com.team7.ticket_booth.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Find by associated booking id (Payment has one-to-one Booking)
    Optional<Payment> findByBooking_Id(Long bookingId);

    // Find all payments by user's id through booking relation
    List<Payment> findByBooking_User_Id(Long userId);

    // Access payment status/total fields correctly
    @Query("SELECT p.paymentStatus FROM Payment p WHERE p.id = ?1")
    String findStatusById(Long paymentId);

    @Query("SELECT p.paymentStatus FROM Payment p WHERE p.booking.id = ?1")
    String findStatusByBookingId(Long bookingId);

    @Query("SELECT p.total FROM Payment p WHERE p.id = ?1")
    Integer findTotalById(Long paymentId);

    @Query("SELECT SUM(p.total) FROM Payment p WHERE p.booking.user.id = ?1")
    Integer findSumOfTotalByUserId(Long userId);
}
