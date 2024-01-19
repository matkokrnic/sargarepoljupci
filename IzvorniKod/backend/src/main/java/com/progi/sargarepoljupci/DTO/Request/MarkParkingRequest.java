package com.progi.sargarepoljupci.DTO.Request;

import com.progi.sargarepoljupci.Utilities.ParkingSpotReservable;
import lombok.Data;

import java.util.List;

@Data
public class MarkParkingRequest {
    private Long parkingId;
    private List<ParkingSpotReservable> parkingSpotReservableList;
}
