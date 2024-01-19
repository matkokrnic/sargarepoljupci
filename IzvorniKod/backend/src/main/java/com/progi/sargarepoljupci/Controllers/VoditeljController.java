package com.progi.sargarepoljupci.Controllers;


import com.progi.sargarepoljupci.DTO.Request.ParkingInformationRequest;
import com.progi.sargarepoljupci.DTO.Request.ReservableUpdateRequest;
import com.progi.sargarepoljupci.Exceptions.RequestDeniedException;
import com.progi.sargarepoljupci.Repository.ParkingRepository;
import com.progi.sargarepoljupci.Services.ParkingService;
import com.progi.sargarepoljupci.Services.ParkingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/voditelj")
public class VoditeljController {
    private final ParkingService parkingService;
    private final ParkingSpotService parkingSpotService;
    private final ParkingRepository parkingRepository;


    @Autowired
    public VoditeljController(ParkingService parkingService, ParkingSpotService parkingSpotService, ParkingRepository parkingRepository) {
        this.parkingService = parkingService;
        this.parkingSpotService = parkingSpotService;
        this.parkingRepository = parkingRepository;

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
    public ResponseEntity<?> enterParkingInformation(@RequestBody ParkingInformationRequest request) throws SQLException, IOException {
        var createParking = parkingService.createNewParking(request);
        parkingService.markSpots(request, createParking);
        return ResponseEntity.ok(createParking);

    }

    @PutMapping("/updateImage/{id}")
    public ResponseEntity<String> updateImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
       var parkingOpt = parkingRepository.findById(id);
       if(parkingOpt.isEmpty()){
           throw new RequestDeniedException("Parking with that id doesn't exist");
       }
       var parking = parkingOpt.get();
       parking.setPicture(file.getBytes());
       return ResponseEntity.ok("Successfully updated picture");
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
