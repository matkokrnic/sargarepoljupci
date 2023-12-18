package com.progi.sargarepoljupci.DTO.Request;


import jakarta.persistence.Lob;
import lombok.Data;

import java.sql.Blob;

@Data
public class ParkingInformationRequest {
    String name;
    String description;
    //@Lob
    //private Blob photo;
    private String photo;
    private double costPerHour;
}
