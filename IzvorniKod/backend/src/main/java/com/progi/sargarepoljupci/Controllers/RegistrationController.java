package com.progi.sargarepoljupci.Controllers;

import com.progi.sargarepoljupci.Models.EmailSender;
import com.progi.sargarepoljupci.Models.Korisnik;
import com.progi.sargarepoljupci.Services.KorisnikService;
import com.progi.sargarepoljupci.Services.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
@Slf4j
public class RegistrationController {

    private final KorisnikService korisnikService;
    private final RegistrationService registrationService;
    private final EmailSender emailSender;


    @Autowired
    public RegistrationController(KorisnikService korisnikService, RegistrationService registrationService, EmailSender emailSender) {
        this.korisnikService = korisnikService;

        this.registrationService = registrationService;
        this.emailSender = emailSender;
    }

    @PostMapping
    public ResponseEntity<String> registerKorisnik(@RequestBody Korisnik korisnik) {
//        return new ResponseEntity<>("OK", HttpStatus.OK);



        if (korisnikService.doesKorisnikExist(korisnik.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email vec postoji.");

        } else {
            log.info("dildo");
            log.info(String.valueOf(korisnik));

            String token = korisnikService.createKorisnik(korisnik);
            emailSender.send(
                    korisnik.getEmail(),
                    buildEmail(korisnik.getIme(), ));
            return ResponseEntity.status(HttpStatus.CREATED).body(token);
        }
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }




}

