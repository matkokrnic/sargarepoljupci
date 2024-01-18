package com.progi.sargarepoljupci.DTO.Response;

import com.progi.sargarepoljupci.Models.BicycleParking;
import lombok.Data;

@Data
public class BicycleParkingResponse {
    private String bicycleId;
    private int numAvailableSpots;
    private double longitude;
    private double latitude;
    private String polygon;

    public BicycleParkingResponse() {

    }

    public BicycleParkingResponse(String bicycleId, int numAvailableSpots, double longitude, double latitude, String polygon) {
        this.bicycleId = bicycleId;
        this.numAvailableSpots = numAvailableSpots;
        this.longitude = longitude;
        this.latitude = latitude;
        this.polygon = polygon;
    }

    public BicycleParkingResponse(BicycleParking bicycleParking) {
        this.bicycleId = bicycleParking.getBicycle_id();
        this.numAvailableSpots = bicycleParking.getNumAvailableSpots();
        this.longitude = bicycleParking.getLongitude();
        this.latitude = bicycleParking.getLatitude();
        this.polygon = bicycleParking.getPolygon();
    }
}
