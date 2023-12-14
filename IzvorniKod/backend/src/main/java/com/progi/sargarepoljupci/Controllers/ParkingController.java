package com.progi.sargarepoljupci.Controllers;


import com.progi.sargarepoljupci.DTO.Request.ClientRequest;
import com.progi.sargarepoljupci.DTO.Response.LocationResponse;
import com.progi.sargarepoljupci.Services.OsrmService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParkingController {

    private OsrmService osrmService;


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









}
