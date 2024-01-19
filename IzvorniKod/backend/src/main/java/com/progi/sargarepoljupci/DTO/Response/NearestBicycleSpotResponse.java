package com.progi.sargarepoljupci.DTO.Response;


import lombok.Data;

@Data
public class NearestBicycleSpotResponse {
    private Double latitude;
    private Double longitude;
    private String parkingSpotID;
    private int numOfAvailableSpots;


    public NearestBicycleSpotResponse(Double latitude, Double longitude, String parkingSpotID, int numOfAvailableSpots) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.parkingSpotID = parkingSpotID;
        this.numOfAvailableSpots = numOfAvailableSpots;
    }
}
