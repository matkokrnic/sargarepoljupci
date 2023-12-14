package com.progi.sargarepoljupci.Models;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ParkingAuto {

    @Id
    @GeneratedValue
    @Column(name = "parking_id")
    private Long id;

    private String picture;
    private String parkingName;
    private String parkingDescription;
    private double costPerHour;

    @OneToOne
    @JoinColumn(name = "voditelj_id")
    private Voditelj voditeljParkiralista;

}
