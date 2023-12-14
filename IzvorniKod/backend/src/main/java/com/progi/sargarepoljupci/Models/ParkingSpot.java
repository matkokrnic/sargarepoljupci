package com.progi.sargarepoljupci.Models;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class ParkingSpot {
    @Id
    @Column(name = "parking_spot_id")
    Long id;

    private String label;
    private double longitude;
    private double latitude;
    private boolean accessible;
    private boolean free;

    @ManyToOne
    @JoinColumn(name = "parking_id")
    private ParkingAuto parking;




}
