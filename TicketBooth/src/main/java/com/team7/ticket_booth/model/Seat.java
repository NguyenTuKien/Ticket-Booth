package com.team7.ticket_booth.model;

import com.team7.ticket_booth.model.enums.SeatType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "seats", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"name", "hallId"})
       })
public class Seat {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "position", nullable = false, length = 10)
    private String position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hallId", nullable = false)
    private Hall hall;

    @Column(name = "seatType", nullable = false)
    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> tickets;
}
