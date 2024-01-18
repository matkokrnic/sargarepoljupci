package com.progi.sargarepoljupci.DTO.Response;


import lombok.Data;

@Data
public class NearestSpotResponse {
    private Double latitude;
    private Double longitude;
    private Boolean isReservable;
    private String parkingSpotID;
    private Boolean insufficientFunds;


    public NearestSpotResponse(Double latitude, Double longitude, Boolean isReservable, String parkingSpotID, Boolean insufficientFunds) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.isReservable = isReservable;
        this.parkingSpotID = parkingSpotID;
        this.insufficientFunds = insufficientFunds;

    }
}
