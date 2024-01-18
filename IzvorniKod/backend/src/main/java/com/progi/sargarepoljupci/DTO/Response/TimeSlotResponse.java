package com.progi.sargarepoljupci.DTO.Response;

import com.progi.sargarepoljupci.Models.Reservation;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class TimeSlotResponse {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String parkingSpotId;

    public TimeSlotResponse(LocalDateTime startTime, LocalDateTime endTime, String parkingSpotId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.parkingSpotId = parkingSpotId;
    }

    public TimeSlotResponse() {
    }
    public TimeSlotResponse(Reservation reservation) {
        this.startTime = reservation.getReservationStart();
        this.endTime = reservation.getReservationEnd();
        this.parkingSpotId = reservation.getParkingSpot().getId();
    }

}
