package com.progi.sargarepoljupci.Models;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Voditelj {


    @Id
    @Column(name = "voditelj_id")
    private String voditeljId;

    @OneToOne
    @JoinColumn(name = "parking_id")
    private ParkingAuto parking;


}
