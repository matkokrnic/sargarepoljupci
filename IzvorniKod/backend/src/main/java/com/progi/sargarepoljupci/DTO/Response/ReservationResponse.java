package com.progi.sargarepoljupci.DTO.Response;

import com.progi.sargarepoljupci.Models.Reservation;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationResponse {
    private Long reservationId;
    private LocalDateTime reservationStart;
    private LocalDateTime reservationEnd;
    private Long korisnikId;
    private String parkingSpotId;

    // Constructors, getters, and setters...

    // You can create a constructor to easily map from a Reservation entity
    public ReservationResponse(Reservation reservation) {
        this.reservationId = reservation.getId();
        this.reservationStart = reservation.getReservationStart();
        this.reservationEnd = reservation.getReservationEnd();
        this.korisnikId = reservation.getKorisnik().getId();
        this.parkingSpotId = reservation.getParkingSpot().getId();
    }
}
