package com.progi.sargarepoljupci.DTO.Request;

import lombok.Data;

import java.util.List;

@Data
public class ReservationRequest {
        private Long userID;
        private String parkingSpotId;
        private List<TimeSlot> timeSlots;
        private Boolean onLocation;

}
