package com.progi.sargarepoljupci.Controllers;

import com.progi.sargarepoljupci.Models.Korisnik;
import com.progi.sargarepoljupci.Repository.KorisnikRepository;
import com.progi.sargarepoljupci.Services.KorisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    private final KorisnikService korisnikService;


    @Autowired
    public RegistrationController(KorisnikService korisnikService) {
        this.korisnikService = korisnikService;

    }

    @PostMapping
    public ResponseEntity<String> registerKorisnik(@RequestBody Korisnik korisnik){
        if(korisnikService.doesKorisnikExist(korisnik.getEmail())){
            korisnikService.createKorisnik(korisnik);
            return ResponseEntity.status(HttpStatus.CREATED).body("Stvorili smo korisnika " + korisnik);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email vec postoji.");
        }


    }




}

