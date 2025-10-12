package com.team7.ticket_booth.model;

import com.team7.ticket_booth.model.enums.CardType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Cards")
public class Card {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "cardNumber", length = 4)
    private String cardNumber;

    @Column(name = "holdersName", length = 100)
    private String holdersName;

    @Column(name = "expirationDate")
    @Temporal(TemporalType.DATE)
    private Date expirationDate;

    @Column(name = "CardType", length = 20)
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @Column(name = "token", length = 255)
    private String token;
}
