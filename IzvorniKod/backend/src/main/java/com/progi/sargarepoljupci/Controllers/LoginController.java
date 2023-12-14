package com.progi.sargarepoljupci.Controllers;


import com.progi.sargarepoljupci.Exceptions.RequestDeniedException;
import com.progi.sargarepoljupci.Models.Korisnik;
import com.progi.sargarepoljupci.DTO.loginDTO;
import com.progi.sargarepoljupci.Models.uloga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final com.progi.sargarepoljupci.Services.korisnikService korisnikService;

    private final com.progi.sargarepoljupci.Repository.korisnikRepository korisnikRepository;
    private final PasswordEncoder encoder;
    @Autowired
    public LoginController(com.progi.sargarepoljupci.Services.korisnikService korisnikService, com.progi.sargarepoljupci.Repository.korisnikRepository korisnikRepository, PasswordEncoder encoder) {
        this.korisnikService = korisnikService;
        this.korisnikRepository = korisnikRepository;
        this.encoder = encoder;
    }

    @PostMapping
    public ResponseEntity<String> loginKorisnik(@RequestBody loginDTO loginDTO){

        System.out.println("usli smo ovdje");
        if (loginDTO.getKorisnickoIme().equals("admin") && encoder.matches("123", loginDTO.getLozinka())){
            return ResponseEntity.status(HttpStatus.valueOf(201)).body("Admin Ulogiran");
        }
        Optional<Korisnik> existingUser = korisnikRepository.findByKorisnickoIme(loginDTO.getKorisnickoIme());
        // ako je username isti
        if(existingUser.isPresent()){
            if (existingUser.get().getVerificiran() == null){
                throw new RequestDeniedException("Korisnik se nije verificirao mailom");
            }
            if (existingUser.get().getUloga() == uloga.VODITELJ && (existingUser.get().getPotvrden()==null ||!existingUser.get().getPotvrden())){
                throw new RequestDeniedException("Voditelj nije prihvacen od strane admina");
            }
            // ako je lozinka ista
            if(encoder.matches(loginDTO.getLozinka(), existingUser.get().getLozinka())){
               return ResponseEntity.status(HttpStatus.valueOf(201)).body("Ulogiran");
            }

            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uneseni podatci su neispravni");
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Korisnik s tim korisnickim imenom ne postoji");
        }

    }



}
