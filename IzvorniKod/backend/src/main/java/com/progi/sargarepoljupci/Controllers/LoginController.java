package com.progi.sargarepoljupci.Controllers;


import com.progi.sargarepoljupci.Models.Korisnik;
import com.progi.sargarepoljupci.Repository.KorisnikRepository;
import com.progi.sargarepoljupci.Services.KorisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final KorisnikService korisnikService;

    private final KorisnikRepository korisnikRepository;
    private final PasswordEncoder encoder;
    @Autowired
    public LoginController(KorisnikService korisnikService, KorisnikRepository korisnikRepository, PasswordEncoder encoder) {
        this.korisnikService = korisnikService;
        this.korisnikRepository = korisnikRepository;
        this.encoder = encoder;
    }

    @PostMapping
    public ResponseEntity<String> loginKorisnik(@RequestBody Korisnik korisnik){
        Optional<Korisnik> existingUser = korisnikRepository.findByEmail(korisnik.getEmail());
        if(existingUser.isPresent()){
            if(encoder.matches(korisnik.getEmail(), existingUser.get().getEmail()) && korisnik.getKorisnickoIme().equals(existingUser.get().getKorisnickoIme())){
               return ResponseEntity.status(HttpStatus.ACCEPTED).body("Ulogiran");
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uneseni podatci su neispravni");
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Korisnik ne postoji");
        }

    }



}
