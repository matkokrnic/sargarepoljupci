package com.progi.sargarepoljupci.DTO.Request;

import lombok.Data;


@Data
public class ClientRequest {
    String destination;
    String vehicleType;
    String parkingDuration;
}
