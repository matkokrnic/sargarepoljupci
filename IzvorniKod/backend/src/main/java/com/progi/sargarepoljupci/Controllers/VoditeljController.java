package com.progi.sargarepoljupci.Controllers;


import com.progi.sargarepoljupci.DTO.Request.ParkingInformationRequest;
import com.progi.sargarepoljupci.DTO.Request.ReservableUpdateRequest;
import com.progi.sargarepoljupci.Exceptions.RequestDeniedException;
import com.progi.sargarepoljupci.Services.ParkingService;
import com.progi.sargarepoljupci.Services.ParkingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/voditelj")
public class VoditeljController {
    private final ParkingService parkingService;
    private final ParkingSpotService parkingSpotService;

    @Autowired
    public VoditeljController(ParkingService parkingService, ParkingSpotService parkingSpotService) {
        this.parkingService = parkingService;
        this.parkingSpotService = parkingSpotService;
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
        var createParking = parkingService.createNewParking(request);
        parkingService.markSpots(request, createParking);
        return ResponseEntity.ok(createParking);

    }


    @PutMapping("/update-reservable")
    public ResponseEntity<String> updateReservableStatus(@RequestBody ReservableUpdateRequest updateDTO) {
        String parkingSpotId = updateDTO.getParkingSpotId();
        Boolean reservable = updateDTO.getReservable();

        try {
            parkingSpotService.updateReservableStatus(parkingSpotId, reservable);
            return ResponseEntity.ok("Reservable status updated successfully for Parking Spot ID: " + parkingSpotId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update Reservable status for Parking Spot ID: " + parkingSpotId);
        }
    }
    @PutMapping("/update-reservable-multiple")
    public ResponseEntity<String> updateReservableStatusForMultipleSpots(@RequestBody List<ReservableUpdateRequest> updateDTOList) {
        try {
            var list = parkingSpotService.updateReservableStatusForMultipleSpots(updateDTOList);
            return ResponseEntity.ok("Reservable status updated successfully for multiple parking spots.");
        }
        catch (Exception e) {
            throw new RequestDeniedException(e.getMessage());
        }

    }






}
