package com.progi.sargarepoljupci.Controllers;


import com.progi.sargarepoljupci.DTO.Request.ParkingInformationRequest;
import com.progi.sargarepoljupci.Models.ParkingAuto;
import com.progi.sargarepoljupci.Repository.ParkingAutoRepository;
import com.progi.sargarepoljupci.Services.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/voditelj")
public class VoditeljController {
    private final ParkingService parkingService;
    private final ParkingAutoRepository autoRepository;
    @Autowired
    public VoditeljController(ParkingService parkingService, ParkingAutoRepository autoRepository) {
        this.parkingService = parkingService;
        this.autoRepository = autoRepository;
    }

/*
    // ucrtaj dostupno parkirno mjesto
    @PostMapping
    public ResponseEntity<?> makeParkingAvailable(){
        return null;
    }

 */
// Voditelj parkinga ima mogućnost unijeti informacije o svom parkiralištu (naziv, opis,
// fotografija, cjenik)
    @PutMapping("/newParking")
    public ResponseEntity<?> enterParkingInformation(@RequestBody ParkingInformationRequest request){
        autoRepository.save(new ParkingAuto(request));
        return ResponseEntity.ok(request);
    }



}
