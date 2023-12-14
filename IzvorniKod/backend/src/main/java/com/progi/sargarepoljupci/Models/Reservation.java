package com.progi.sargarepoljupci.Models;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;

    double price;
    Timestamp reservationStart;
    Timestamp reservationEnd;
    @Nullable
    String RRULE;

    @ManyToOne
    @JoinColumn(name = "korisnik_id")
    private Korisnik korisnik;


    @ManyToOne
    @JoinColumn(name = "parking_spot_id")
    private ParkingSpot parkingSpot;



}
