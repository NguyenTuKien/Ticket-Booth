package com.team7.ticket_booth.model;

import com.team7.ticket_booth.model.enums.Method;
import com.team7.ticket_booth.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookingId", nullable = false)
    private Booking booking;

    @Column(name = "total", nullable = false)
    private int total;

    @Column(name = "createdAt", nullable = false, updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "paymentMethod", nullable = false)
    @Enumerated(EnumType.STRING)
    private Method paymentMethod;

    @Column(name = "paymentStatus", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status paymentStatus;
}

