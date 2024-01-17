package com.progi.sargarepoljupci.Controllers;


import com.progi.sargarepoljupci.DTO.Request.TimeSlotRequest;
import com.progi.sargarepoljupci.Models.ParkingAuto;
import com.progi.sargarepoljupci.Models.ParkingSpot;
import com.progi.sargarepoljupci.Models.Reservation;
import com.progi.sargarepoljupci.Repository.ParkingAutoRepository;
import com.progi.sargarepoljupci.Repository.ParkingSpotRepository;
import com.progi.sargarepoljupci.Repository.ReservationRepository;
import com.progi.sargarepoljupci.Services.ParkingSpotService;
import com.progi.sargarepoljupci.Services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@RequestMapping("/api/reservation")
@RestController
public class ReservationController {
    private final ParkingSpotService parkingSpotService;
    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingAutoRepository parkingAutoRepository;

    @Autowired
    public ReservationController(ParkingSpotService parkingSpotService, ReservationService reservationService, ReservationRepository reservationRepository, ParkingSpotRepository parkingSpotRepository, ParkingAutoRepository parkingAutoRepository) {
        this.parkingSpotService = parkingSpotService;
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
        this.parkingSpotRepository = parkingSpotRepository;
        this.parkingAutoRepository = parkingAutoRepository;
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
    // testiranje
    @GetMapping("/reservations")
    public List<Reservation> findOverlappingReservations(
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        return reservationService.findOverlappingReservations(startTime, endTime);

    }

    @PostMapping("/findAvailableParkingSpots")
    public List<ParkingSpot> findAvailableParkingSpots(@RequestBody List<TimeSlotRequest> timeSlots) {
        return reservationService.findAvailableParkingSpotsForTimeSlots(timeSlots);
    }

    @PostMapping("/unavailableTimeslots")
    public List<Reservation> findReservationsForParkingSpots(@RequestBody List<String> parkingSpotIds) {
        return reservationRepository.findByParkingSpotIdIn(parkingSpotIds);
    }
    @GetMapping("/accessibleParkingSpots")
    public List<ParkingSpot> findAccessibleParkingSpots() {
        return parkingSpotRepository.findParkingSpotsByAccessibleIsTrue();
    }

    @GetMapping("/parkingLots")
    public List<ParkingAuto> retrieveParkingLots() {
        return parkingAutoRepository.findAll();
    }







}
