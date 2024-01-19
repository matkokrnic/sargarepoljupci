package com.progi.sargarepoljupci.Models;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class ParkingSpot {
    // Identifikacija mjesta koja smo dobili pomocu
    // overpassAPI. Npr. ”node/11310562209” ili "way/874978501"

    @Id
    @Column(name = "parking_spot_id")
    String id;

    private String label;
    private double longitude;
    private double latitude;
    private String polygon;
    private Boolean reservable;
    //private Boolean free;

    @ManyToOne
    @JoinColumn(name = "parking_id")
    private Parking parking;


}
