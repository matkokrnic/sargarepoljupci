package com.progi.sargarepoljupci.Controllers;

import com.progi.sargarepoljupci.DTO.Request.DepositRequest;
import com.progi.sargarepoljupci.DTO.Request.NearestSpotRequest;
import com.progi.sargarepoljupci.DTO.Response.NearestSpotResponse;
import com.progi.sargarepoljupci.Exceptions.RequestDeniedException;
import com.progi.sargarepoljupci.Repository.BicycleRepository;
import com.progi.sargarepoljupci.Repository.ParkingSpotRepository;
import com.progi.sargarepoljupci.Services.ParkingService;
import com.progi.sargarepoljupci.Services.ReservationService;
import com.progi.sargarepoljupci.Services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/client")
public class ClientController {
    private final WalletService walletService;
    private final ParkingService parkingService;
    private final ParkingSpotRepository parkingSpotRepository;
    private final ReservationService reservationService;
    private final BicycleRepository bicycleRepository;
    @Autowired
    public ClientController(WalletService walletService, ParkingService parkingService, ParkingSpotRepository parkingSpotRepository, ReservationService reservationService, BicycleRepository bicycleRepository) {
        this.walletService = walletService;
        this.parkingService = parkingService;
        this.parkingSpotRepository = parkingSpotRepository;
        this.reservationService = reservationService;
        this.bicycleRepository = bicycleRepository;
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
    public ResponseEntity<NearestSpotResponse> findNearestParkingSpot(
            @RequestBody NearestSpotRequest nearestSpotRequest) {
        var vehicleType = nearestSpotRequest.getVehicleType();
        //var currentTime = nearestSpotRequest.getCurrentTime();
        var parkingDurationInMinutes = nearestSpotRequest.getParkingDurationInMinutes();
        var destination = nearestSpotRequest.getLatitude()+","+nearestSpotRequest.getLongitude();
        var currentTime = LocalDateTime.now();



        // ako su bicikli onda samo vratimo koordinate najblizeg, ne trebamo radit nista sto se tice rezervacije
        if(vehicleType.equalsIgnoreCase("bicycle")){
            var nearest= parkingService.findNearestBicycleSpot(destination);
            var bicycleParking = bicycleRepository.findByLongitudeAndLatitude(nearest.getFirst(), nearest.getSecond());
            if(bicycleParking==null)
                throw new RequestDeniedException("There's no available bicycle parking");
            return ResponseEntity.ok(new NearestSpotResponse(nearest.getFirst(), nearest.getSecond(), false, bicycleParking.getBicycle_id()));
        }
        currentTime = reservationService.roundToClosest30Minutes(currentTime);
       Pair<Double, Double> nearestCoordinates = parkingService.findNearestAvailableParking(destination, currentTime);
        // provjeriti je li rezervirano mjesto, prvo nadjemo mjesto
       var parkingSpot = parkingSpotRepository.findByLongitudeAndLatitude(nearestCoordinates.getFirst(), nearestCoordinates.getSecond());
        LocalDateTime reservationEnd = currentTime.plusMinutes(parkingDurationInMinutes);



        var reservable = reservationService.canParkingSpotBeReserved(parkingSpot.getId(), currentTime,  reservationEnd) && parkingSpot.getReservable();
        //if(reservable){
        //    reservationService.makeMultipleReservations(List.of(new TimeSlotRequest(currentTime, currentTime.plusMinutes(parkingDurationInMinutes))), )
        //}
        return ResponseEntity.ok(new NearestSpotResponse(nearestCoordinates.getFirst(), nearestCoordinates.getSecond(), reservable, parkingSpot.getId()));

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
