package com.progi.sargarepoljupci.Controllers;


import com.progi.sargarepoljupci.DTO.Request.ReservationRequest;
import com.progi.sargarepoljupci.DTO.Request.TimeSlot;
import com.progi.sargarepoljupci.Exceptions.RequestDeniedException;
import com.progi.sargarepoljupci.Models.ParkingSpot;
import com.progi.sargarepoljupci.Models.Reservation;
import com.progi.sargarepoljupci.Repository.ParkingAutoRepository;
import com.progi.sargarepoljupci.Repository.ParkingSpotRepository;
import com.progi.sargarepoljupci.Repository.ReservationRepository;
import com.progi.sargarepoljupci.Services.ParkingSpotService;
import com.progi.sargarepoljupci.Services.ReservationService;
import com.progi.sargarepoljupci.Services.korisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/reservation")
@RestController
public class ReservationController {
    private final ParkingSpotService parkingSpotService;
    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingAutoRepository parkingAutoRepository;
    private final korisnikService korisnikService;

    @Autowired
    public ReservationController(ParkingSpotService parkingSpotService, ReservationService reservationService, ReservationRepository reservationRepository, ParkingSpotRepository parkingSpotRepository, ParkingAutoRepository parkingAutoRepository, com.progi.sargarepoljupci.Services.korisnikService korisnikService) {
        this.parkingSpotService = parkingSpotService;
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
        this.parkingSpotRepository = parkingSpotRepository;
        this.parkingAutoRepository = parkingAutoRepository;
        this.korisnikService = korisnikService;
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
    public List<ParkingSpot> findAvailableParkingSpots(@RequestBody List<TimeSlot> timeSlots) {
        return reservationService.findReservableParkingSpotsForTimeSlots(timeSlots);
    }

    @PostMapping("/unavailableTimeslots")
    public List<TimeSlot> findReservationsForParkingSpots(@RequestBody List<String> parkingSpotIds) {
        var temp =  reservationRepository.findByParkingSpotIdIn(parkingSpotIds);
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


    //@GetMapping("/parkingLots")
    //public List<ParkingAuto> retrieveParkingLots() {
    //    return parkingAutoRepository.findAll();
    //}


    // return:
    // 1. If all timeslots are available, HTTP.ok i koliko treba platiti u bodyu
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
        // ako mjesto nije postavljeno kao reservable i ako user nije na lokaciji
        if(!parkingSpot.get().getReservable() && !(request.getOnLocation())){
            throw new RequestDeniedException("Parking spot hasn't been set as reservable");
        }




        // ima listu nedostupnih timeslotova kao key i totalDuration kao value
        // koristim u ovom slucaju mapu samo kao Pair
        var unavailableTimeSlots = reservationService.makeMultipleReservations(userID, parkingSpotId, timeSlots);

        // U slucaju kad su svi timeslotovi dostupni, trebam izracunat koliko treba platiti
        if(unavailableTimeSlots.getKey()==null){
            var totalDuration = unavailableTimeSlots.getValue();

            var costPerHour = parkingSpot.get().getParking().getCostPerHour();
            double totalPrice = costPerHour*(totalDuration.doubleValue()/60.);


            return ResponseEntity.ok(totalPrice);
        }else
            return ResponseEntity.badRequest().body(unavailableTimeSlots.getKey());



        // AT FIRST I THOUGHT THAT I SHOULD RETURN THE ACCEPTED RESERVATIONS, BUT NO ONE CARES ABOUT THAT
        // IT MAKES MORE SENSE TO JUST RETURN 201 IF EVERYTHING WAS ACCEPTED, AND REQUEST DENIED WITH
        // THE LIST OF TIMESLOTS THAT WEREN'T ACCEPTED IN THE OTHER CASE
        // BUT THERE ARE A LOT OF OPTIONS HERE

        //for (Reservation reservation : reservations) {
        //    responseList.add(new ReservationResponse(reservation));
        //}



    }






}
