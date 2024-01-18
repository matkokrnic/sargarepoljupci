package com.progi.sargarepoljupci.DTO.Request;

import lombok.Data;

@Data
public class ReservableUpdateRequest {
    private String parkingSpotId;
    private Boolean reservable;
}
