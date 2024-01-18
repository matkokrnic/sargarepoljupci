package com.progi.sargarepoljupci.Models;



import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Voditelj {

    // povezati ovo s korisnikom
    @Id
    private Long voditeljId;

    /*
        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "autoparking_id", referencedColumnName = "parking_id")
        private ParkingAuto parking;


     */
    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "voditelj_id")
    private Korisnik korisnik;




}
