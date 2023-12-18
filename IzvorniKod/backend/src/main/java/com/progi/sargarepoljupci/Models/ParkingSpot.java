package com.progi.sargarepoljupci.Models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;


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
    private Boolean accessible;
    private Boolean free;

    @ManyToOne
    @JoinColumn(name = "parking_id")
    private ParkingAuto parking;


}
