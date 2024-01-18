package com.progi.sargarepoljupci.Controllers;


import com.progi.sargarepoljupci.DTO.Response.BicycleParkingResponse;
import com.progi.sargarepoljupci.DTO.Response.ParkingResponse;
import com.progi.sargarepoljupci.DTO.Response.ParkingSpotResponse;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @GetMapping("/availability")
    public ResponseEntity<Boolean> getParkingSpotAvailability(@RequestBody String parkingSpotId) {
        boolean availability = parkingSpotService.getParkingSpotAvailability(parkingSpotId);
        return ResponseEntity.ok(availability);
    }

    @GetMapping("/accessibleParkingSpots")
    public List<ParkingSpotResponse> findAccessibleParkingSpots() {
        List<ParkingSpot> parkingSpots = parkingSpotRepository.findParkingSpotsByParkingIsNotNull();
        List<ParkingSpotResponse> parkingSpotRequests = new ArrayList<>();

        for (ParkingSpot parkingSpot : parkingSpots) {
            parkingSpotRequests.add(new ParkingSpotResponse(parkingSpot));
        }
        return parkingSpotRequests;
    }
    @GetMapping("/accessibleBicycleSpots")
    public List<BicycleParkingResponse> findAccessibleBicycleSpots() {
        List<BicycleParking> parkingSpots = bicycleRepository.findByParkingLotIsNotNull();
        List<BicycleParkingResponse> parkingSpotRequests = new ArrayList<>();

        for (BicycleParking parkingSpot : parkingSpots) {
            parkingSpotRequests.add(new BicycleParkingResponse(parkingSpot));
        }
        return parkingSpotRequests;
    }

    @GetMapping("/unmarkedParkingSpots")
    public List<ParkingSpotResponse> findUnmarkedParkingSpots() {
        List<ParkingSpot> parkingSpots = parkingSpotRepository.findParkingSpotsByParkingIsNull();
        List<ParkingSpotResponse> parkingSpotRequests = new ArrayList<>();

        for (ParkingSpot parkingSpot : parkingSpots) {
            parkingSpotRequests.add(new ParkingSpotResponse(parkingSpot));
        }
        return parkingSpotRequests;
    }

    @GetMapping("/unmarkedBicycleSpots")
    public List<BicycleParkingResponse> findUnmarkedBicycleSpots() {
        List<BicycleParking> parkingSpots = bicycleRepository.findByParkingLotIsNull();
        List<BicycleParkingResponse> parkingSpotRequests = new ArrayList<>();

        for (BicycleParking parkingSpot : parkingSpots) {
            parkingSpotRequests.add(new BicycleParkingResponse(parkingSpot));
        }
        return parkingSpotRequests;
    }

    @GetMapping("/reservableParkingSpots")
    public List<ParkingSpotResponse> findReservableParkingSpots() {
        List<ParkingSpot> parkingSpots = parkingSpotRepository.findParkingSpotsByReservableIsTrue();
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
        List<ParkingSpot> parkingSpots = parkingSpotRepository.findAll();
        List<ParkingSpotResponse> parkingSpotRequests = new ArrayList<>();

        for (ParkingSpot parkingSpot : parkingSpots) {
            parkingSpotRequests.add(new ParkingSpotResponse(parkingSpot));
        }

        return ResponseEntity.ok(parkingSpotRequests);
    }

    @GetMapping("/all-bicycle-spots")
    public ResponseEntity<List<?>> getAllBicycleSpots() {
        List<BicycleParking> bicycleParkingList = bicycleRepository.findAll();
        List<BicycleParkingResponse> bicycleParkingRequests = new ArrayList<>();

        for (BicycleParking bicycleParking : bicycleParkingList) {
            bicycleParkingRequests.add(new BicycleParkingResponse(bicycleParking));
        }

        return ResponseEntity.ok(bicycleParkingRequests);
    }

    @GetMapping("/parking-spots/by-parking/{parkingId}")
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
    public List<ParkingResponse> retrieveParkingLots() {

        List<Parking> parkingList = parkingRepository.findAll();
        List<ParkingResponse> responseList = new ArrayList<>();

        for (Parking parking : parkingList) {
            responseList.add(new ParkingResponse(parking));
        }
        return responseList;
    }



}
