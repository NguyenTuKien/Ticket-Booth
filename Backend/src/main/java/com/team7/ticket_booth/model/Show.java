package com.team7.ticket_booth.model;

import com.team7.ticket_booth.model.enums.Shift;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shows", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"movieId", "hallId", "day", "shift"})
       })
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movieId", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hallId", nullable = false)
    private Hall hall;

    @Column(name = "shift", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Shift shift;

    @Column(name = "day", nullable = false)
    private LocalDate day;

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> tickets;
}
