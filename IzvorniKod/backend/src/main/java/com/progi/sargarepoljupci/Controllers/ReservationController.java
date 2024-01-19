package com.progi.sargarepoljupci.Controllers;


import com.progi.sargarepoljupci.DTO.Request.ReservationRequest;
import com.progi.sargarepoljupci.DTO.Request.TimeSlot;
import com.progi.sargarepoljupci.DTO.Response.ParkingSpotResponse;
import com.progi.sargarepoljupci.Exceptions.RequestDeniedException;
import com.progi.sargarepoljupci.Models.ParkingSpot;
import com.progi.sargarepoljupci.Models.Reservation;
import com.progi.sargarepoljupci.Repository.ParkingSpotRepository;
import com.progi.sargarepoljupci.Services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/reservation")
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    private final ParkingSpotRepository parkingSpotRepository;

    @Autowired
    public ReservationController(ReservationService reservationService, ParkingSpotRepository parkingSpotRepository) {

        this.reservationService = reservationService;
        this.parkingSpotRepository = parkingSpotRepository;

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
    // provjera
    @GetMapping("/reservations")
    public List<Reservation> findOverlappingReservations(
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        return reservationService.findOverlappingReservations(startTime, endTime);

    }

    @PostMapping("/findReservableParkingSpots")
    public List<ParkingSpotResponse> findAvailableParkingSpots(@RequestBody List<TimeSlot> timeSlots) {
        var parkingSpots = reservationService.findReservableParkingSpotsForTimeSlots(timeSlots);
        List<ParkingSpotResponse> parkingSpotRequests = new ArrayList<>();

        for (ParkingSpot parkingSpot : parkingSpots) {
            parkingSpotRequests.add(new ParkingSpotResponse(parkingSpot));
        }
        return parkingSpotRequests;
    }

    @PostMapping("/unavailableTimeslots")
    public List<TimeSlot> findReservationsForParkingSpots(@RequestBody List<String> parkingSpotIds) {
        var temp =  reservationService.findByParkingSpotIdIn(parkingSpotIds);
        return temp.stream()
                .map(TimeSlot::new)
                .collect(Collectors.toList());

    }
    /*
    @GetMapping("/accessibleParkingSpots")
    public List<ParkingSpot> findAccessibleParkingSpots() {
        return parkingSpotRepository.findParkingSpotsByAccessibleIsTrue();
    }

     */





    // return:
    // 1. If all timeslots are available, HTTP.ok
    // 2. If some timeslots aren't available: Bad Request with the List of unavailable Timeslots
    @PostMapping("/create-reservations")
    public ResponseEntity<?> makeMultipleReservations(
            @RequestBody ReservationRequest request
    ) {

        var userID = request.getUserID();
        String parkingSpotId = request.getParkingSpotId();
        List<TimeSlot> timeSlots = request.getTimeSlots();

        var parkingSpot =parkingSpotRepository.findById(parkingSpotId);
        if(parkingSpot.isEmpty()){
            throw new RequestDeniedException("Parking spot doesn't exist");
        }
        if(parkingSpot.get().getParking()==null){
            throw new RequestDeniedException("Parking spot doesn't belong to any parking lot");
        }
        // ako mjesto nije postavljeno kao reservable i ako user nije na lokaciji
        //ako je false ili null i ako user nije na lokaciji
        var temp = parkingSpot.get().getReservable();
        var onLocation = request.getOnLocation();
        if((temp ==null || !temp) && (onLocation==null || !onLocation)){
            throw new RequestDeniedException("Parking spot hasn't been set as reservable");
        }





        var unavailableTimeSlots = reservationService.makeMultipleReservations(userID, parkingSpotId, timeSlots);


        if(unavailableTimeSlots==null){
           return ResponseEntity.ok("Successfully reserved");

        }else
            return ResponseEntity.badRequest().body(unavailableTimeSlots);



        // AT FIRST I THOUGHT THAT I SHOULD RETURN THE ACCEPTED RESERVATIONS, BUT NO ONE CARES ABOUT THAT
        // IT MAKES MORE SENSE TO JUST RETURN 201 IF EVERYTHING WAS ACCEPTED, AND REQUEST DENIED WITH
        // THE LIST OF TIMESLOTS THAT WEREN'T ACCEPTED IN THE OTHER CASE
        // BUT THERE ARE A LOT OF OPTIONS HERE

        //for (Reservation reservation : reservations) {
        //    responseList.add(new ReservationResponse(reservation));
        //}



    }






}
