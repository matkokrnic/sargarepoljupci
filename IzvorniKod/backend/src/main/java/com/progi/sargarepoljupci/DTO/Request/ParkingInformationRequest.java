package com.progi.sargarepoljupci.DTO.Request;


import com.progi.sargarepoljupci.Utilities.ParkingSpotReservable;
import lombok.Data;

import java.util.List;

@Data
public class ParkingInformationRequest {
    String name;
    String description;
    //@Lob
    //private Blob photo;
    private String photo;
    private double costPerHour;
    private long voditeljID;
    List<ParkingSpotReservable> parkingSpotList;
}
