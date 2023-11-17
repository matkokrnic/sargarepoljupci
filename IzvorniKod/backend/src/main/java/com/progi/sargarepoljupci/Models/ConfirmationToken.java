package com.progi.sargarepoljupci.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Getter
@NoArgsConstructor
@Setter
public class ConfirmationToken {

    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "korisnik_id"
    )
    private Korisnik korisnik;

    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, Korisnik korisnik) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.korisnik = korisnik;
    }
}
