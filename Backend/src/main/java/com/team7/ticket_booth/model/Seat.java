package com.team7.ticket_booth.model;

import com.team7.ticket_booth.model.enums.TypeOfSeat;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 10)
    private String position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hallId", nullable = false)
    private Hall hall;

    @Column(name = "typeOfSeat", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeOfSeat typeOfSeat;

    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> tickets;
}
