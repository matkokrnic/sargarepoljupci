package com.progi.sargarepoljupci.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime reservationStart;
    private LocalDateTime reservationEnd;
    private Integer duration;


    @ManyToOne
    @JoinColumn(name = "korisnik_id")
    private Korisnik korisnik;


    @ManyToOne
    @JoinColumn(name = "parking_spot_id")
    private ParkingSpot parkingSpot;

}
