package com.progi.sargarepoljupci.Controllers;

import com.progi.sargarepoljupci.Models.Korisnik;
import com.progi.sargarepoljupci.Repository.KorisnikRepository;
import com.progi.sargarepoljupci.Services.KorisnikService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RegistrationController {

    private final KorisnikService korisnikService;


    @Autowired
    public RegistrationController(KorisnikService korisnikService) {
        this.korisnikService = korisnikService;

    }

    @PostMapping
    public ResponseEntity<String> registerKorisnik(@RequestBody Korisnik korisnik) {
//        return new ResponseEntity<>("OK", HttpStatus.OK);

        if (korisnikService.doesKorisnikExist(korisnik.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email vec postoji.");

        } else {
            log.info("dildo");
            log.info(String.valueOf(korisnik));

            korisnikService.createKorisnik(korisnik);
            return ResponseEntity.status(HttpStatus.CREATED).body("Stvorili smo korisnika " + korisnik);
        }
    }


}

