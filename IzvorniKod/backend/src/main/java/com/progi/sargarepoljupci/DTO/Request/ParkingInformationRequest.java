package com.progi.sargarepoljupci.DTO.Request;


import com.progi.sargarepoljupci.Utilities.ParkingSpotReservable;
import lombok.Data;

import java.util.List;

@Data
public class ParkingInformationRequest {
    String name;
    String description;

    private double costPerHour;
    private long voditeljID;
    List<ParkingSpotReservable> parkingSpotList;
}
