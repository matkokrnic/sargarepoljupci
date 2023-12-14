package com.progi.sargarepoljupci.Models;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class BicycleParking {
    @Id
    @GeneratedValue
    private  Long id;

    @Column(name = "number_of_spots")
    private int numAvailableSpots;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "latitude")
    private double latitude;


}

