package com.progi.sargarepoljupci.Controllers;


import com.progi.sargarepoljupci.DTO.Request.MarkParkingRequest;
import com.progi.sargarepoljupci.DTO.Response.BicycleParkingResponse;
import com.progi.sargarepoljupci.DTO.Response.ParkingResponse;
import com.progi.sargarepoljupci.DTO.Response.ParkingSpotResponse;
import com.progi.sargarepoljupci.Exceptions.RequestDeniedException;
import com.progi.sargarepoljupci.Models.BicycleParking;
import com.progi.sargarepoljupci.Models.Parking;
import com.progi.sargarepoljupci.Models.ParkingSpot;
import com.progi.sargarepoljupci.Services.BicycleService;
import com.progi.sargarepoljupci.Services.ParkingService;
import com.progi.sargarepoljupci.Services.ParkingSpotService;
import com.progi.sargarepoljupci.Services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ParkingController {

    private final ParkingSpotService parkingSpotService;

    private final ReservationService reservationService;
    private final BicycleService bicycleService;
    private final ParkingService parkingService;
    @Autowired
    public ParkingController(ParkingSpotService parkingSpotService, ReservationService reservationService, BicycleService bicycleService, ParkingService parkingService) {

        this.parkingSpotService = parkingSpotService;
        this.reservationService = reservationService;
        this.bicycleService = bicycleService;
        this.parkingService = parkingService;
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

    @GetMapping("/availability")
    public ResponseEntity<Boolean> getParkingSpotAvailability(@RequestBody String parkingSpotId) {
        boolean availability = parkingSpotService.getParkingSpotAvailability(parkingSpotId);
        return ResponseEntity.ok(availability);
    }

    @GetMapping("/accessibleParkingSpots")
    public List<ParkingSpotResponse> findAccessibleParkingSpots() {
        List<ParkingSpot> parkingSpots = parkingSpotService.findParkingSpotsByParkingIsNotNull();
        List<ParkingSpotResponse> parkingSpotRequests = new ArrayList<>();

        for (ParkingSpot parkingSpot : parkingSpots) {
            parkingSpotRequests.add(new ParkingSpotResponse(parkingSpot));
        }
        return parkingSpotRequests;
    }
    @GetMapping("/accessibleBicycleSpots")
    public List<BicycleParkingResponse> findAccessibleBicycleSpots() {
        List<BicycleParking> parkingSpots = bicycleService.findByParkingLotIsNotNull();
        List<BicycleParkingResponse> parkingSpotRequests = new ArrayList<>();

        for (BicycleParking parkingSpot : parkingSpots) {
            parkingSpotRequests.add(new BicycleParkingResponse(parkingSpot));
        }
        return parkingSpotRequests;
    }

    @GetMapping("/unmarkedParkingSpots")
    public List<ParkingSpotResponse> findUnmarkedParkingSpots() {
        List<ParkingSpot> parkingSpots = parkingSpotService.findParkingSpotsByParkingIsNull();
        List<ParkingSpotResponse> parkingSpotRequests = new ArrayList<>();

        for (ParkingSpot parkingSpot : parkingSpots) {
            parkingSpotRequests.add(new ParkingSpotResponse(parkingSpot));
        }
        return parkingSpotRequests;
    }

    @GetMapping("/unmarkedBicycleSpots")
    public List<BicycleParkingResponse> findUnmarkedBicycleSpots() {
        List<BicycleParking> parkingSpots = bicycleService.findByParkingLotIsNull();
        List<BicycleParkingResponse> parkingSpotRequests = new ArrayList<>();

        for (BicycleParking parkingSpot : parkingSpots) {
            parkingSpotRequests.add(new BicycleParkingResponse(parkingSpot));
        }
        return parkingSpotRequests;
    }

    @GetMapping("/reservableParkingSpots")
    public List<ParkingSpotResponse> findReservableParkingSpots() {
        List<ParkingSpot> parkingSpots = parkingSpotService.findParkingSpotsByReservableIsTrue();
        List<ParkingSpotResponse> parkingSpotRequests = new ArrayList<>();

        for (ParkingSpot parkingSpot : parkingSpots) {
            parkingSpotRequests.add(new ParkingSpotResponse(parkingSpot));
        }
        return parkingSpotRequests;
    }

    @GetMapping("/occupied")
    public List<ParkingSpotResponse> getOccupiedParkingSpots() {
        List<ParkingSpot> parkingSpots = reservationService.findReservedParkingSpotsForTimeSlot(LocalDateTime.now(), LocalDateTime.now());
        List<ParkingSpotResponse> parkingSpotRequests = new ArrayList<>();

        for (ParkingSpot parkingSpot : parkingSpots) {
            parkingSpotRequests.add(new ParkingSpotResponse(parkingSpot));
        }
        return parkingSpotRequests;

    }

    // Endpoint to get unoccupied parking spots
    @GetMapping("/unoccupied")
    public List<ParkingSpotResponse> getUnoccupiedParkingSpots() {
        List<ParkingSpot> parkingSpots = reservationService.findAvailableParkingSpotsForTimeSlot(LocalDateTime.now(), LocalDateTime.now());
        List<ParkingSpotResponse> parkingSpotRequests = new ArrayList<>();

        for (ParkingSpot parkingSpot : parkingSpots) {
            parkingSpotRequests.add(new ParkingSpotResponse(parkingSpot));
        }
        return parkingSpotRequests;

    }

    @GetMapping("/all-parking-spots")
    public ResponseEntity<List<ParkingSpotResponse>> getAllParkingSpots() {
        List<ParkingSpot> parkingSpots = parkingSpotService.findAll();
        List<ParkingSpotResponse> parkingSpotRequests = new ArrayList<>();

        for (ParkingSpot parkingSpot : parkingSpots) {
            parkingSpotRequests.add(new ParkingSpotResponse(parkingSpot));
        }

        return ResponseEntity.ok(parkingSpotRequests);
    }

    @GetMapping("/all-bicycle-spots")
    public ResponseEntity<List<?>> getAllBicycleSpots() {
        List<BicycleParking> bicycleParkingList = bicycleService.findAll();
        List<BicycleParkingResponse> bicycleParkingRequests = new ArrayList<>();

        for (BicycleParking bicycleParking : bicycleParkingList) {
            bicycleParkingRequests.add(new BicycleParkingResponse(bicycleParking));
        }

        return ResponseEntity.ok(bicycleParkingRequests);
    }

    @GetMapping("/parking-spots/for-parking/{parkingId}")
    public ResponseEntity<List<ParkingSpotResponse>> getAllParkingSpotsForParking(@PathVariable Long parkingId) {
        List<ParkingSpot> parkingSpots = parkingService.getAllParkingSpotsForParking(parkingId);
        List<ParkingSpotResponse> parkingSpotResponses = new ArrayList<>();

        for (ParkingSpot parkingSpot : parkingSpots) {
            parkingSpotResponses.add(new ParkingSpotResponse(parkingSpot));
        }

        return ResponseEntity.ok(parkingSpotResponses);

    }
    @GetMapping("/bicycle-spots/for-parking/{parkingId}")
    public ResponseEntity<List<?>> getAllBicycleSpotsForParking(@PathVariable Long parkingId) {
        List<BicycleParking> bicycleParkingList = parkingService.getAllBicycleSpotsForParking(parkingId);

        List<BicycleParkingResponse> bicycleParkingRequests = new ArrayList<>();

        for (BicycleParking bicycleParking : bicycleParkingList) {
            bicycleParkingRequests.add(new BicycleParkingResponse(bicycleParking));
        }

        return ResponseEntity.ok(bicycleParkingRequests);
    }

    @GetMapping("/parkingLots")
    public ResponseEntity<List<ParkingResponse>> retrieveParkingLots() {

        List<Parking> parkingList = parkingService.findAll();
        List<ParkingResponse> responseList = new ArrayList<>();

        for (Parking parking : parkingList) {
            responseList.add(new ParkingResponse(parking));
        }
        return ResponseEntity.ok(responseList);
    }
    @GetMapping("/parkingLot/{parking_id}")
    public ParkingResponse retrieveParkingLot(@PathVariable Long parking_id) {
        var parkingOptional = parkingService.findById(parking_id);
        if(parkingOptional.isEmpty())
            throw new RequestDeniedException("Parking with that id doesn't exist");
        var parking = parkingOptional.get();
        return new ParkingResponse(parking);

    }
    @PostMapping("/mark")
    public ResponseEntity<?> markParkingSpots(@RequestBody MarkParkingRequest request) {
        var unmarkedSpots = parkingService.markSpots(request);
        if(unmarkedSpots == null){
           return ResponseEntity.ok("All spots were marked");
       }
        return ResponseEntity.badRequest().body(unmarkedSpots);

    }



}
