package com.progi.sargarepoljupci.DTO.Request;

import com.progi.sargarepoljupci.Models.Reservation;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class TimeSlot {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public TimeSlot(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimeSlot() {
    }
    public TimeSlot(Reservation reservation) {
        this.startTime = reservation.getReservationStart();
        this.endTime = reservation.getReservationEnd();
    }

}
