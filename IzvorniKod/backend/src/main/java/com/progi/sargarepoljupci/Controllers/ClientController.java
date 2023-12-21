package com.progi.sargarepoljupci.Controllers;

import com.progi.sargarepoljupci.DTO.Request.CalendarReservationRequest;
import com.progi.sargarepoljupci.DTO.Request.DepositRequest;
import com.progi.sargarepoljupci.Services.ParkingService;
import com.progi.sargarepoljupci.Services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@RestController
public class ClientController {
    private final WalletService walletService;
    private final ParkingService parkingService;
    @Autowired
    public ClientController(WalletService walletService, ParkingService parkingService) {
        this.walletService = walletService;
        this.parkingService = parkingService;
    }


    // rezervacija putem kalendara
    // parametri: parkiralisna mjesta za koja je klijent zainteresiran, odnosno lista parkiralisnih mjesta
    //
    public void calendarReservation(@RequestBody CalendarReservationRequest request){

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

    @GetMapping("/findNearest")
    public ResponseEntity<?> findNearestParkingSpot(
            @RequestParam("destination") String destination,
            @RequestParam("vehicleType") String vehicleType,
            //@RequestParam("currentTime") LocalDateTime currentTime,
            @RequestParam("parkingDuration") int parkingDurationInMinutes) {
       return ResponseEntity.ok(parkingService.findNearestAvailableParking(destination, vehicleType, parkingDurationInMinutes));


    }






}
