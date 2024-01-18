package com.progi.sargarepoljupci.Models;


import com.progi.sargarepoljupci.DTO.Request.ParkingInformationRequest;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ParkingAuto {

    //@Id
    //private String parkingId;
    //@Column(columnDefinition = "TEXT")
    //private String polygon;

    @Id
    @GeneratedValue
    private Long parkingId;

    private String picture;
    private String parkingName;
    private String parkingDescription;
    private Double costPerHour;

    @Nullable
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "voditelj_id")
    private Voditelj voditelj;


 /*
    @OneToOne
    @JoinColumn(name = "voditelj_id")
    private Voditelj voditeljParkiralista;

 */

    public ParkingAuto() {
    }


    public ParkingAuto(ParkingInformationRequest request) {
        this.parkingName= request.getName();
        this.parkingDescription = request.getDescription();
        this.costPerHour = request.getCostPerHour();
        this.picture = request.getPhoto();

    }

}
