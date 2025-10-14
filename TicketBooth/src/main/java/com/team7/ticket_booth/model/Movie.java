package com.team7.ticket_booth.model;

import com.team7.ticket_booth.model.enums.Genre;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.type.descriptor.jdbc.NVarcharJdbcType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false, columnDefinition = "NVARCHAR(200)")
    private String title;

    @Column(name = "genre")
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Column(name = "thumbnailUrl", length = 500)
    private String thumbnailUrl;

    @Column (name = "trailerUrl", length = 500)
    private String trailerUrl;

    @Column(name = "description", columnDefinition = "NTEXT")
    private String description;

    @Column(name = "duration", nullable = false)
    private int duration; // duration in minutes

    @Column (name = "rating")
    private float rating;

    @Column(name = "releaseDay")
    private LocalDate beginDay;

    @Column(name = "closeDay")
    private LocalDate endDay;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Show> shows;
}
