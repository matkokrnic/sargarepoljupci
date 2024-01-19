package com.progi.sargarepoljupci.DTO.Request;


import lombok.Data;

@Data
public class ParkingInformationRequest {
    private String name;
    private String description;

    private double costPerHour;
    private long voditeljID;
    //List<ParkingSpotReservable> parkingSpotList;

    public ParkingInformationRequest(String name, String description, double costPerHour, long voditeljID) {
        this.name = name;
        this.description = description;
        this.costPerHour = costPerHour;
        this.voditeljID = voditeljID;
    }

    public ParkingInformationRequest() {
    }
}
