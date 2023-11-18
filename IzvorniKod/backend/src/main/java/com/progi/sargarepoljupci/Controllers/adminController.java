package com.progi.sargarepoljupci.Controllers;

import com.progi.sargarepoljupci.Exceptions.korisnikNotFoundException;
import com.progi.sargarepoljupci.Exceptions.requestDeniedException;
import com.progi.sargarepoljupci.Models.korisnik;
import com.progi.sargarepoljupci.Models.uloga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@Secured("ROLE_ADMIN")
@RequestMapping("/api/admin")
public class adminController {

    private final com.progi.sargarepoljupci.Repository.korisnikRepository korisnikRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.progi.sargarepoljupci.Services.korisnikService korisnikService;
    @Autowired
    public adminController(com.progi.sargarepoljupci.Repository.korisnikRepository korisnikRepository, PasswordEncoder passwordEncoder, com.progi.sargarepoljupci.Services.korisnikService korisnikService) {
        this.korisnikRepository = korisnikRepository;
        this.passwordEncoder = passwordEncoder;
        this.korisnikService = korisnikService;
    }


    @GetMapping("/korisnici")
    public List<korisnik> getAllUsers() {
        return korisnikRepository.findAll();
    }
    /*
    @GetMapping("/voditelji")
    public List<Korisnik> getAllUsers() {
        return korisnikRepository.findAll();
    }
    */


    @GetMapping("/neprihvaceni")
    public List<korisnik> listNeprihvaceniVoditelji(){
        return korisnikService.findByVoditeljNotApproved();
    }

    //kljucno:
    // ne smije biti novo ime vec u bazi podataka
    // novi email isto ne smije biti vec u bazi podataka

    // ovdje bi trebao napraviti DTO
    @PutMapping("/update/{id}")
    // ovdje bi trebao napraviti DTO
    public korisnik updateKorisnik(@PathVariable("id") Long id, @RequestBody korisnik requestKorisnik) {

        korisnik korisnikInBaza = korisnikRepository.findById(id).orElse(null);
        if (korisnikInBaza != null) {
            Long requestKorisnikId = requestKorisnik.getId();

            // provjera je li id isti kao i id usera koji smo poslali u bodyu
            if(!Objects.equals(requestKorisnikId, id)){
                throw new requestDeniedException("Nepravilan id");
            }

            // ovo da ako je lozinka == null stavljam u onu koja je bila
            // ne trebam tako tretirati, ali o tome nekom drugom prilikom
            if(requestKorisnik.getLozinka() == null) {
                requestKorisnik.setLozinka(korisnikInBaza.getLozinka());
            } else {
                requestKorisnik.setLozinka(passwordEncoder.encode(requestKorisnik.getLozinka()));
            }

            // gledamo postoji li neki drugi user koji ima taj mail ali drukciji id
            if(korisnikRepository.existsByEmailAndIdIsNot(requestKorisnik.getEmail(), id)){
                throw new requestDeniedException("Neki drugi korisnik vec ima taj email");
            }
            // gledamo postoji li neki drugi user koji ima to korisnickoIme ali drukciji id
            if(korisnikRepository.existsByKorisnickoImeAndIdIsNot(requestKorisnik.getEmail(), id)){
                throw new requestDeniedException("Neki drugi korisnik vec ima to korisnickoIme");
            }
            return korisnikRepository.save(requestKorisnik);

        }else
            throw new requestDeniedException("Taj korisnik ne postoji u bazi (nema id-a)");
    }



    @PutMapping("/approve/{id}")
    public korisnik approveUser(@PathVariable("id") Long id) {
        System.out.println("Sad smo u approve");

        Optional<korisnik> optionalVoditelj = korisnikRepository.findById(id);

        if(optionalVoditelj.isPresent()) {
            korisnik korisnik = optionalVoditelj.get();
            if(korisnik.getUloga() != uloga.VODITELJ){
                throw new requestDeniedException("Taj korisnik nije voditelj pa ga ne mozete potvrditi");
            }
            // ako nije potvrden potvrdi ga
            else if(korisnik.getPotvrden() == null || !korisnik.getPotvrden() ) {
                korisnik.setPotvrden(true);
                return korisnikRepository.save(korisnik);
            }
            else throw new requestDeniedException("Voditelj je vec potvrden");

        } else{

            throw new korisnikNotFoundException("Korisnik s tim id-om "+ id +" ne postoji");

        }

    }

    @PutMapping("/decline/{id}")
    public korisnik declineUser(@PathVariable("id") Long id) {

        Optional<korisnik> optionalVoditelj = korisnikRepository.findById(id);

        if(optionalVoditelj.isPresent()) {
            korisnik korisnik = optionalVoditelj.get();
            if(korisnik.getUloga() != uloga.VODITELJ){
                throw new requestDeniedException("Taj korisnik nije voditelj pa ga ne mozete odbiti");
            }
            else if(korisnik.getPotvrden() == null || korisnik.getPotvrden()) {
                korisnik.setPotvrden(false);
                System.out.println("Korisnik je odijbne");
                return korisnikRepository.save(korisnik);
            }
            else throw new requestDeniedException("Voditelj je vec odbijen");

        } else{

            throw new korisnikNotFoundException("Korisnik s tim id-om "+ id +" ne postoji");

        }

    }









}


