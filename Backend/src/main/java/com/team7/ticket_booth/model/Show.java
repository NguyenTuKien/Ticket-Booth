package com.team7.ticket_booth.model;

import com.team7.ticket_booth.model.enums.Shift;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shows", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"movieId", "hallId", "day", "shift"})
       })
public class Show {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movieId", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hallId")
    private Hall hall;

    @Column(name = "shift", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Shift shift;

    @Column(name = "day", nullable = false)
    private LocalDate showDate;

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> tickets;
}
