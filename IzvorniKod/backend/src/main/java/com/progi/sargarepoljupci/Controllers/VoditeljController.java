package com.progi.sargarepoljupci.Controllers;


import com.progi.sargarepoljupci.DTO.Request.AccessibleUpdateRequest;
import com.progi.sargarepoljupci.DTO.Request.ParkingInformationRequest;
import com.progi.sargarepoljupci.Models.ParkingAuto;
import com.progi.sargarepoljupci.Repository.ParkingAutoRepository;
import com.progi.sargarepoljupci.Services.ParkingService;
import com.progi.sargarepoljupci.Services.ParkingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/voditelj")
public class VoditeljController {
    private final ParkingService parkingService;
    private final ParkingSpotService parkingSpotService;
    private final ParkingAutoRepository autoRepository;
    @Autowired
    public VoditeljController(ParkingService parkingService, ParkingSpotService parkingSpotService, ParkingAutoRepository autoRepository) {
        this.parkingService = parkingService;
        this.parkingSpotService = parkingSpotService;
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


    @PutMapping("/update-accessible")
    public ResponseEntity<String> updateAccessibleStatus(@RequestBody AccessibleUpdateRequest updateDTO) {
        String parkingSpotId = updateDTO.getParkingSpotId();
        Boolean accessible = updateDTO.getAccessible();

        try {
            parkingSpotService.updateAccessibleStatus(parkingSpotId, accessible);
            return ResponseEntity.ok("Accessible status updated successfully for Parking Spot ID: " + parkingSpotId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update accessible status for Parking Spot ID: " + parkingSpotId);
        }
    }


}
