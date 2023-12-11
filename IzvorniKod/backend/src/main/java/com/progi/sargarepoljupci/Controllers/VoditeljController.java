package com.progi.sargarepoljupci.Controllers;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/voditelj")
public class VoditeljController {

    // ucrtaj dostupno parkirno mjesto
    @PostMapping
    public void makeParkingAvailable(){

    }
    @PutMapping
    public void enterParkingInformation(){


    }

}
