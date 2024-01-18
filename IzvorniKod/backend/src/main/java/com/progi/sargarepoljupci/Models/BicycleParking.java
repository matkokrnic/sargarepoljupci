package com.progi.sargarepoljupci.Models;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "bicycle_parking")
public class BicycleParking {
    @Id
    @GeneratedValue
    private  String bicycle_id;

    @Column(name = "number_of_spots")
    private int numAvailableSpots;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "latitude")
    private double latitude;

    private String polygon;

    @ManyToOne
    @JoinColumn(name = "parking_id")
    private Parking parkingLot;


}

