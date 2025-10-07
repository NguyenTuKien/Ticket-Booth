package com.team7.ticket_booth.repository;

import com.team7.ticket_booth.model.Order;
import com.team7.ticket_booth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT o FROM Order o WHERE o.user = :user AND (o.payment IS NOT NULL AND o.payment.status = 'SUCCESS')")
    List<Order> findOrderSuccessByUser(User user);
}
