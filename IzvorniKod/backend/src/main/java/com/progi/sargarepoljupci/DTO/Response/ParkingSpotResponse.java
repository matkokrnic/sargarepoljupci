package com.progi.sargarepoljupci.DTO.Response;


import com.progi.sargarepoljupci.Models.ParkingSpot;
import lombok.Data;

@Data
public class ParkingSpotResponse {

    private String id;
    private String label;
    private double longitude;
    private double latitude;
    private String polygon;
    private Boolean reservable;

    public ParkingSpotResponse(ParkingSpot parkingSpot) {
        this.id = parkingSpot.getId();
        this.label = parkingSpot.getLabel();
        this.longitude = parkingSpot.getLongitude();
        this.latitude = parkingSpot.getLatitude();
        this.polygon = parkingSpot.getPolygon();
        this.reservable = parkingSpot.getReservable();
    }

    public ParkingSpotResponse(String id, String label, double longitude, double latitude, String polygon, Boolean reservable) {
        this.id = id;
        this.label = label;
        this.longitude = longitude;
        this.latitude = latitude;
        this.polygon = polygon;
        this.reservable = reservable;
    }

    public ParkingSpotResponse() {
    }
}
