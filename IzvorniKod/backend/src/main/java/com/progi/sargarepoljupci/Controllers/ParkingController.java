package com.progi.sargarepoljupci.Controllers;


import com.progi.sargarepoljupci.DTO.Request.ClientRequest;
import com.progi.sargarepoljupci.DTO.Response.LocationResponse;
import com.progi.sargarepoljupci.Services.OsrmService;
import com.progi.sargarepoljupci.Services.ParkingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ParkingController {

    private OsrmService osrmService;
    private final ParkingSpotService parkingSpotService;
    @Autowired
    public ParkingController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @PostMapping("/{parkingSpotId}/updateAvailability")
    public ResponseEntity<String> updateParkingSpotAvailability(@PathVariable String parkingSpotId, @RequestBody boolean setFree) {
        var updateSuccessful = parkingSpotService.updateParkingSpotAvailability(parkingSpotId, setFree);

        if (updateSuccessful) {
            return ResponseEntity.ok("Parking spot availability updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update parking spot availability");
        }
    }

    @GetMapping("/{parkingSpotId}/availability")
    public ResponseEntity<Boolean> getParkingSpotAvailability(@PathVariable String parkingSpotId) {
        boolean availability = parkingSpotService.getParkingSpotAvailability(parkingSpotId);
        return ResponseEntity.ok(availability);
    }




    /*
    Pregledavanjem karte, klijent može odabrati lokaciju svog odredišta, tip vozila i
    procjenu trajanja parkinga, a aplikacija mu na karti iscrta rutu do najbližeg slobodnog
    parkirališnog mjesta i rezervira ga ako je slobodno za rezervaciju. Za dohvat rute do
    parkirališnog mjesta potrebno je koristiti OSRM.

    ovo nema bas smisla
     */

    // parametri: lokacija odredista, tip vozila (auto/bicikl), procjena trajanja parkinga
    // return: lokacija slobodnog mjesta i informacija jesam li ga rezervirao ili ne
    @PostMapping("/api/calculateRoute")
    public LocationResponse minDistanceToAvailableSpace(@RequestBody ClientRequest request){

        // prvo trebamo filtirati sva slobodna mjesta
        // findByAvailable()



        return osrmService.minDistanceToParkingSpot(request);

    }
/*
    @GetMapping
    public void getFreeParkingSpots(){

    }

    @GetMapping
    public void getAccessibleParkingSpots(){

    }

 */

    // pregledavanje parkiralisnog mjesta koje je dostupno








}
