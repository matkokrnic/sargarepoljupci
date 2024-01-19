package com.progi.sargarepoljupci.Controllers;

import com.progi.sargarepoljupci.DTO.Request.DepositRequest;
import com.progi.sargarepoljupci.DTO.Request.NearestSpotRequest;
import com.progi.sargarepoljupci.DTO.Request.TimeSlot;
import com.progi.sargarepoljupci.DTO.Response.NearestBicycleSpotResponse;
import com.progi.sargarepoljupci.DTO.Response.NearestSpotResponse;
import com.progi.sargarepoljupci.Exceptions.RequestDeniedException;
import com.progi.sargarepoljupci.Services.BicycleService;
import com.progi.sargarepoljupci.Services.ParkingSpotService;
import com.progi.sargarepoljupci.Services.ParkingService;
import com.progi.sargarepoljupci.Services.ReservationService;
import com.progi.sargarepoljupci.Services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/client")
public class ClientController {
    private final WalletService walletService;
    private final ParkingService parkingService;
    private final ParkingSpotService parkingSpotService;
    private final ReservationService reservationService;
    private final BicycleService bicycleService;
    @Autowired
    public ClientController(WalletService walletService, ParkingService parkingService, ParkingSpotService parkingSpotService, ReservationService reservationService, BicycleService bicycleService) {
        this.walletService = walletService;
        this.parkingService = parkingService;
        this.parkingSpotService = parkingSpotService;
        this.reservationService = reservationService;
        this.bicycleService = bicycleService;
    }



    // nadopuna novcanika
    @PutMapping("/{userId}/deposit")
    public ResponseEntity<String> depositToWallet(@PathVariable Long userId, @RequestBody DepositRequest request) {
        boolean depositSuccessful = walletService.depositFunds(userId, request.getDepositAmount());

        if (depositSuccessful) {
            return ResponseEntity.ok("Funds deposited successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to deposit funds");
        }
    }

    // ovo treba vratiti coordinate i boolean je li rezervirano mjesto
    @GetMapping("/findNearest")
    public ResponseEntity<?> findNearestParkingSpot(
            @RequestBody NearestSpotRequest nearestSpotRequest) {
        var vehicleType = nearestSpotRequest.getVehicleType();
        //var currentTime = nearestSpotRequest.getCurrentTime();
        var parkingDurationInMinutes = nearestSpotRequest.getParkingDurationInMinutes();
        var destination = nearestSpotRequest.getLongitude()+","+nearestSpotRequest.getLatitude();
        var currentTime = LocalDateTime.now();



        // ako su bicikli onda samo vratimo koordinate najblizeg, ne trebamo radit nista sto se tice rezervacije
        if(vehicleType.equalsIgnoreCase("bicycle")){
            var nearest= parkingService.findNearestBicycleSpot(destination);
            var bicycleParking = bicycleService.findByLongitudeAndLatitude(nearest.getFirst(), nearest.getSecond());
            if(bicycleParking==null)
                throw new RequestDeniedException("There's no available bicycle parking");
            return ResponseEntity.ok(new NearestBicycleSpotResponse(nearest.getFirst(), nearest.getSecond(), bicycleParking.getBicycle_id(), bicycleParking.getNumAvailableSpots()));
        }
        currentTime = reservationService.roundToClosest30Minutes(currentTime);
       Pair<Double, Double> nearestCoordinates = parkingService.findNearestAvailableParking(destination, currentTime);
        // provjeriti je li rezervirano mjesto, prvo nadjemo mjesto
       var parkingSpot = parkingSpotService.findByLongitudeAndLatitude(nearestCoordinates.getFirst(), nearestCoordinates.getSecond());
        LocalDateTime reservationEnd = currentTime.plusMinutes(parkingDurationInMinutes);
        boolean reservable;
        reservable = reservationService.canParkingSpotBeReserved(parkingSpot.getId(), currentTime,  reservationEnd) && parkingSpot.getReservable();
        if(reservable){
            var timeslot = new TimeSlot(currentTime, currentTime.plusMinutes(parkingDurationInMinutes));
            try {
                reservationService.makeMultipleReservations(nearestSpotRequest.getUserId(), parkingSpot.getId(), List.of(timeslot));

                    return ResponseEntity.ok(new NearestSpotResponse(parkingSpot.getLatitude(), parkingSpot.getLongitude(), true, parkingSpot.getId(), false));

            }catch (RequestDeniedException e) {
                return ResponseEntity.ok(new NearestSpotResponse(nearestCoordinates.getFirst(), nearestCoordinates.getSecond(), true, parkingSpot.getId(), true));
            }

        }else
           return ResponseEntity.ok(new NearestSpotResponse(nearestCoordinates.getFirst(), nearestCoordinates.getSecond(), false, parkingSpot.getId(), null));



    }

    @PostMapping("/{userId}/subtractBalance")
    public ResponseEntity<String> withdrawFunds(@PathVariable Long userId, @RequestParam double amount) {
        try {
            walletService.withdrawFunds(userId, amount);
            return ResponseEntity.ok("Balance subtracted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }












}
