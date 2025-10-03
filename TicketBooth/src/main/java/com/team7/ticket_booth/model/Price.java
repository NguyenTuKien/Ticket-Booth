package com.team7.ticket_booth.model;

import com.team7.ticket_booth.model.enums.SeatType;
import com.team7.ticket_booth.model.enums.Shift;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "prices")
public class Price {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "seatType", nullable = false)
    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    @Column(name = "shift", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Shift shift;

    @Column(name = "value", nullable = false)
    private int value;
}
