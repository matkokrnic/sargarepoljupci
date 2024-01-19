package com.progi.sargarepoljupci.DTO.Request;

import lombok.Data;

@Data
public class NearestSpotRequest {
    private double longitude;
    private double latitude;
    private String vehicleType;
    //private LocalDateTime currentTime;
    private long parkingDurationInMinutes;
    private long userId;
}
