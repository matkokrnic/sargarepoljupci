package com.progi.sargarepoljupci.DTO.Response;


import lombok.Data;

@Data
public class NearestSpotResponse {
    private Double latitude;
    private Double longitude;
    private Boolean isReservable;
    private String parkingSpotID;

    public NearestSpotResponse(Double latitude, Double longitude, Boolean isReservable, String parkingSpotID) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.isReservable = isReservable;
        this.parkingSpotID = parkingSpotID;
    }
}
