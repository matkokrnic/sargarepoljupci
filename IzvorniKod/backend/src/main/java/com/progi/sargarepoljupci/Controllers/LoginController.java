package com.progi.sargarepoljupci.Controllers;


import com.progi.sargarepoljupci.DTO.LoginDTO;
import com.progi.sargarepoljupci.Exceptions.RequestDeniedException;
import com.progi.sargarepoljupci.Exceptions.UserNotFoundException;
import com.progi.sargarepoljupci.Models.Korisnik;
import com.progi.sargarepoljupci.DTO.Uloga;
import com.progi.sargarepoljupci.Services.KorisnikService;
import com.progi.sargarepoljupci.Security.JWT2.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/login")
public class LoginController {



    private final KorisnikService korisnikService;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    @Autowired
    public LoginController(KorisnikService korisnikService, PasswordEncoder encoder, AuthenticationManager authenticationManager, JWTService jwtService) {

        this.korisnikService = korisnikService;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<?> loginKorisnik(@RequestBody LoginDTO loginDTO){

        System.out.println("usli smo ovdje");
        if (loginDTO.getKorisnickoIme().equals("admin") && encoder.matches("123", loginDTO.getLozinka())){
            return ResponseEntity.status(HttpStatus.valueOf(201)).body("Admin Ulogiran");
        }
        Optional<Korisnik> existingUser = korisnikService.findByKorisnickoIme(loginDTO.getKorisnickoIme());
        // ako je username isti
        if(existingUser.isPresent()){
            if (existingUser.get().getVerificiran() == null && existingUser.get().getUloga()!= Uloga.ADMIN){
                throw new RequestDeniedException("Korisnik se nije verificirao mailom");
            }
            if (existingUser.get().getUloga() == Uloga.VODITELJ && (existingUser.get().getPotvrden()==null ||!existingUser.get().getPotvrden())){
                throw new RequestDeniedException("Voditelj nije prihvacen od strane admina");
            }
            // ako je lozinka ista
            if(encoder.matches(loginDTO.getLozinka(), existingUser.get().getLozinka())){
                Authentication authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getKorisnickoIme(), loginDTO.getLozinka()));
                if (authentication.isAuthenticated()){
                    return ResponseEntity.ok(jwtService.generateToken(loginDTO.getKorisnickoIme()));
                }
                else {
                    throw new UserNotFoundException("Invalid user credentials");
                }

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
