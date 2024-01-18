package com.progi.sargarepoljupci.Controllers;


import com.progi.sargarepoljupci.Models.BicycleParking;
import com.progi.sargarepoljupci.Models.Parking;
import com.progi.sargarepoljupci.Models.ParkingSpot;
import com.progi.sargarepoljupci.Repository.BicycleRepository;
import com.progi.sargarepoljupci.Repository.ParkingRepository;
import com.progi.sargarepoljupci.Repository.ParkingSpotRepository;
import com.progi.sargarepoljupci.Services.ParkingService;
import com.progi.sargarepoljupci.Services.ParkingSpotService;
import com.progi.sargarepoljupci.Services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class ParkingController {

    private final ParkingSpotService parkingSpotService;
    private final ParkingSpotRepository parkingSpotRepository;
    private final ReservationService reservationService;
    private final BicycleRepository bicycleRepository;
    private final ParkingService parkingService;
    private final ParkingRepository parkingRepository;
    @Autowired
    public ParkingController(ParkingSpotService parkingSpotService, ParkingSpotRepository parkingSpotRepository, ReservationService reservationService, BicycleRepository bicycleRepository, ParkingService parkingService, ParkingRepository parkingRepository) {
        this.parkingSpotService = parkingSpotService;
        this.parkingSpotRepository = parkingSpotRepository;
        this.reservationService = reservationService;
        this.bicycleRepository = bicycleRepository;
        this.parkingService = parkingService;
        this.parkingRepository = parkingRepository;
    }


   // @PostMapping("/{parkingSpotId}/updateAvailability")
   // public ResponseEntity<String> updateParkingSpotAvailability(@PathVariable String parkingSpotId, @RequestBody boolean setFree) {
   //     var updateSuccessful = parkingSpotService.updateParkingSpotAvailability(parkingSpotId, setFree);
//
   //     if (updateSuccessful) {
   //         return ResponseEntity.ok("Parking spot availability updated successfully");
   //     } else {
   //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update parking spot availability");
   //     }
   // }

    @GetMapping("/{parkingSpotId}/availability")
    public ResponseEntity<Boolean> getParkingSpotAvailability(@PathVariable String parkingSpotId) {
        boolean availability = parkingSpotService.getParkingSpotAvailability(parkingSpotId);
        return ResponseEntity.ok(availability);
    }

    @GetMapping("/accessibleParkingSpots")
    public List<ParkingSpot> findAccessibleParkingSpots() {
        return parkingSpotRepository.findParkingSpotsByParkingIsNotNull();
    }

    @GetMapping("/reservableParkingSpots")
    public List<ParkingSpot> findReservableParkingSpots() {
        return parkingSpotRepository.findParkingSpotsByReservableIsTrue();
    }

    @GetMapping("/occupied")
    public List<ParkingSpot> getOccupiedParkingSpots() {
        return reservationService.findReservedParkingSpotsForTimeSlot(LocalDateTime.now(), LocalDateTime.now());

    }

    // Endpoint to get unoccupied parking spots
    @GetMapping("/unoccupied")
    public List<ParkingSpot> getUnoccupiedParkingSpots() {
        return reservationService.findAvailableParkingSpotsForTimeSlot(LocalDateTime.now(), LocalDateTime.now());

    }

    @GetMapping("/parking-spots")
    public ResponseEntity<List<ParkingSpot>> getAllParkingSpots() {
        List<ParkingSpot> allParkingSpots = parkingSpotRepository.findAll();
        return ResponseEntity.ok(allParkingSpots);
    }

    @GetMapping("/bicycle-spots")
    public ResponseEntity<List<?>> getAllBicycleSpots() {
        List<BicycleParking> allBicycleSpots = bicycleRepository.findAll();
        return ResponseEntity.ok(allBicycleSpots);
    }

    @GetMapping("/parking-spots/by-parking/{parkingId}")
    public ResponseEntity<List<ParkingSpot>> getAllParkingSpotsForParking(@PathVariable Long parkingId) {
        List<ParkingSpot> parkingSpots = parkingService.getAllParkingSpotsForParking(parkingId);
        return ResponseEntity.ok(parkingSpots);
    }
    @GetMapping("/bicycle-spots/for-parking/{parkingId}")
    public ResponseEntity<List<?>> getAllBicycleSpotsForParking(@PathVariable Long parkingId) {
        List<BicycleParking> bicycleSpots = parkingService.getAllBicycleSpotsForParking(parkingId);
        return ResponseEntity.ok(bicycleSpots);
    }

    @GetMapping("/parkingLots")
    public List<Parking> retrieveParkingLots() {
        return parkingRepository.findAll();
    }



}
