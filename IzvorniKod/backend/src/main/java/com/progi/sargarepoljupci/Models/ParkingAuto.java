package com.progi.sargarepoljupci.Models;


import com.progi.sargarepoljupci.DTO.Request.ParkingInformationRequest;
import com.progi.sargarepoljupci.DTO.registrationDTO;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ParkingAuto {

    @Id
    private Long parkingVoditelj;

    private String picture;
    private String parkingName;
    private String parkingDescription;
    private double costPerHour;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
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
