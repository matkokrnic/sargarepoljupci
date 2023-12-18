package com.progi.sargarepoljupci.Controllers;


import com.progi.sargarepoljupci.DTO.Response.TimeSlotResponse;
import com.progi.sargarepoljupci.Services.ParkingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReservationController {
    private final ParkingSpotService parkingSpotService;

    @Autowired
    public ReservationController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }
/*
    @PostMapping("/mapSelection")
    public ResponseEntity<List<TimeSlotResponse>> handleMapSelection(@RequestBody List<String> ParkingSpotIds){
        List<TimeSlotResponse> availableTimeSlots = parkingSpotService.checkAvailability(ParkingSpotIds);
    }

    @PostMapping("/timeSelection")
    public ResponseEntity<List<ParkingSpot>> handleTimeSlotSelection(@RequestBody TimeSlotRequest timeSlotRequest) {

        List<ParkingSpot> availableParkingSpots = parkingService.getAvailableParkingSpotsForTimeSlot(timeSlotRequest);

        if (!availableParkingSpots.isEmpty()) {
            return ResponseEntity.ok(availableParkingSpots);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

 */



}
