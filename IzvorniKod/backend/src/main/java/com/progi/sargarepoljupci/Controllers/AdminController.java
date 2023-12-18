package com.progi.sargarepoljupci.Controllers;

import com.progi.sargarepoljupci.Exceptions.UserNotFoundException;
import com.progi.sargarepoljupci.Exceptions.RequestDeniedException;
import com.progi.sargarepoljupci.Models.Korisnik;
import com.progi.sargarepoljupci.Models.Voditelj;
import com.progi.sargarepoljupci.Models.uloga;
import com.progi.sargarepoljupci.Repository.VoditeljRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@Secured("ROLE_ADMIN")
@RequestMapping("/api/admin")
public class AdminController {

    private final com.progi.sargarepoljupci.Repository.korisnikRepository korisnikRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.progi.sargarepoljupci.Services.korisnikService korisnikService;
    private final VoditeljRepository voditeljRepository;
    @Autowired
    public AdminController(com.progi.sargarepoljupci.Repository.korisnikRepository korisnikRepository, PasswordEncoder passwordEncoder, com.progi.sargarepoljupci.Services.korisnikService korisnikService, VoditeljRepository voditeljRepository) {
        this.korisnikRepository = korisnikRepository;
        this.passwordEncoder = passwordEncoder;
        this.korisnikService = korisnikService;
        this.voditeljRepository = voditeljRepository;
    }


    @GetMapping("/korisnici")
    public List<Korisnik> getAllUsers() {
        return korisnikRepository.findAll();
    }


    /*
    @GetMapping("/voditelji")
    public List<Korisnik> getAllUsers() {
        return korisnikRepository.findAll();
    }
    */


    @GetMapping("/neprihvaceni")
    public List<Korisnik> listNeprihvaceniVoditelji(){
        return korisnikService.findByVoditeljNotApproved();
    }

    //kljucno:
    // ne smije biti novo ime vec u bazi podataka
    // novi email isto ne smije biti vec u bazi podataka

    // ovdje bi trebao napraviti DTO
    @PutMapping("/update/{id}")
    // ovdje bi trebao napraviti DTO
    public Korisnik updateKorisnik(@PathVariable("id") Long id, @RequestBody Korisnik requestKorisnik) {

        Korisnik korisnikInBaza = korisnikRepository.findById(id).orElse(null);
        if (korisnikInBaza != null) {
            Long requestKorisnikId = requestKorisnik.getId();

            // provjera je li id isti kao i id usera koji smo poslali u bodyu
            if(!Objects.equals(requestKorisnikId, id)){
                throw new RequestDeniedException("Nepravilan id");
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
                throw new RequestDeniedException("Neki drugi korisnik vec ima taj email");
            }
            // gledamo postoji li neki drugi user koji ima to korisnickoIme ali drukciji id
            if(korisnikRepository.existsByKorisnickoImeAndIdIsNot(requestKorisnik.getEmail(), id)){
                throw new RequestDeniedException("Neki drugi korisnik vec ima to korisnickoIme");
            }
            return korisnikRepository.save(requestKorisnik);

        }else
            throw new RequestDeniedException("Taj korisnik ne postoji u bazi (nema id-a)");
    }



    @PutMapping("/approve/{id}")
    public Korisnik approveUser(@PathVariable("id") Long id) {
        System.out.println("Sad smo u approve");

        Optional<Korisnik> optionalVoditelj = korisnikRepository.findById(id);

        if(optionalVoditelj.isPresent()) {
            Korisnik korisnik = optionalVoditelj.get();
            if(korisnik.getUloga() != uloga.VODITELJ){
                throw new RequestDeniedException("Taj korisnik nije voditelj pa ga ne mozete potvrditi");
            }
            // ako nije potvrden potvrdi ga
            else if(korisnik.getPotvrden() == null || !korisnik.getPotvrden() ) {
                korisnik.setPotvrden(true);
                Voditelj voditelj = new Voditelj();
                voditelj.setKorisnik(korisnik);
                voditeljRepository.save(voditelj);
                return korisnikRepository.save(korisnik);
            }
            else throw new RequestDeniedException("Voditelj je vec potvrden");

        } else{

            throw new UserNotFoundException("Korisnik s tim id-om "+ id +" ne postoji");

        }

    }

    @PutMapping("/decline/{id}")
    public Korisnik declineUser(@PathVariable("id") Long id) {

        Optional<Korisnik> optionalVoditelj = korisnikRepository.findById(id);

        if(optionalVoditelj.isPresent()) {
            Korisnik korisnik = optionalVoditelj.get();
            if(korisnik.getUloga() != uloga.VODITELJ){
                throw new RequestDeniedException("Taj korisnik nije voditelj pa ga ne mozete odbiti");
            }
            else if(korisnik.getPotvrden() == null || korisnik.getPotvrden()) {
                korisnik.setPotvrden(false);
                System.out.println("Korisnik je odbijen");
                return korisnikRepository.save(korisnik);
            }
            else throw new RequestDeniedException("Voditelj je vec odbijen");

        } else{

            throw new UserNotFoundException("Korisnik s tim id-om "+ id +" ne postoji");

        }

    }
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long id){
        try{
            //korisnikRepository.deleteById(id);
            voditeljRepository.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user: " + e.getMessage());
        }
    }









}

