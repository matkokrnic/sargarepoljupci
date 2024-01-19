package com.progi.sargarepoljupci.Controllers;

import com.progi.sargarepoljupci.DTO.Request.PersonalInformationRequest;
import com.progi.sargarepoljupci.DTO.Response.PersonalInformationResponse;
import com.progi.sargarepoljupci.DTO.Uloga;
import com.progi.sargarepoljupci.Exceptions.RequestDeniedException;
import com.progi.sargarepoljupci.Exceptions.UserNotFoundException;
import com.progi.sargarepoljupci.Models.Korisnik;
import com.progi.sargarepoljupci.Models.Voditelj;
import com.progi.sargarepoljupci.Services.KorisnikService;
import com.progi.sargarepoljupci.Services.VoditeljService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Secured("ROLE_ADMIN")
@RequestMapping("/api/admin")
public class AdminController {
    
    private final KorisnikService korisnikService;
    private final VoditeljService voditeljService;
    @Autowired
    public AdminController(KorisnikService korisnikService, VoditeljService voditeljRepository) {
        this.korisnikService = korisnikService;
        this.voditeljService = voditeljRepository;
    }


    @GetMapping("/users")
    public List<PersonalInformationResponse> getAllUsers() {

        List<PersonalInformationResponse> responses = new ArrayList<>();
        List<Korisnik> korisnici = korisnikService.findByVerificiranIsTrue();
        for (Korisnik korisnik : korisnici) {
            PersonalInformationResponse response = new PersonalInformationResponse(korisnik);
            responses.add(response);
        }

        return responses;
    }


    /*
    @GetMapping("/voditelji")
    public List<Korisnik> getAllUsers() {
        return korisnikRepository.findAll();
    }
    */


    @GetMapping("/unapprovedManagers")
    public List<PersonalInformationResponse> listNeprihvaceniVoditelji(){

        var korisnici = korisnikService.findByVoditeljNotApproved();
        List<PersonalInformationResponse> responses = new ArrayList<>();
        for (Korisnik korisnik : korisnici) {
            PersonalInformationResponse response = new PersonalInformationResponse(korisnik);
            responses.add(response);
        }

        return responses;
    }

    @GetMapping("/user/{id}")
    // ovdje bi trebao napraviti DTO
    public ResponseEntity<?> getKorisnik(@PathVariable("id") Long id) {
        var korisnikOptional = korisnikService.findById(id);
        if(korisnikOptional.isEmpty())
            return ResponseEntity.badRequest().body("User with that id doesn't exist");
        var korisnik1= korisnikOptional.get();
        return ResponseEntity.ok(new PersonalInformationResponse(korisnik1));
    }







    //kljucno:
    // ne smije biti novo ime vec u bazi podataka
    // novi email isto ne smije biti vec u bazi podataka

    // ovdje bi trebao napraviti DTO
    @PutMapping("/update/{id}")
    // ovdje bi trebao napraviti DTO
    public ResponseEntity<?> updateKorisnik(@PathVariable("id") Long id, @RequestParam("photo") MultipartFile photo,
                                            @RequestParam("username") String username,
                                            @RequestParam("password") String password,
                                            @RequestParam("firstName") String firstName,
                                            @RequestParam("lastName") String lastName,
                                            @RequestParam("iban") String iban,
                                            @RequestParam("email") String emailAddress){

        PersonalInformationRequest request = new PersonalInformationRequest(username, password, firstName, lastName, photo, iban, emailAddress);
        try {
            korisnikService.updateKorisnik(id, request);
            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user");
        }
    }



    @PutMapping("/approve/{id}")
    public Korisnik approveUser(@PathVariable("id") Long id) {
        System.out.println("Sad smo u approve");

        Optional<Korisnik> optionalVoditelj = korisnikService.findById(id);

        if(optionalVoditelj.isPresent()) {
            Korisnik korisnik = optionalVoditelj.get();
            if(korisnik.getUloga() != Uloga.VODITELJ){
                throw new RequestDeniedException("Taj korisnik nije voditelj pa ga ne mozete potvrditi");
            }
            // ako nije potvrden potvrdi ga
            else if(korisnik.getPotvrden() == null || !korisnik.getPotvrden() ) {
                korisnik.setPotvrden(true);
                Voditelj voditelj = new Voditelj();
                voditelj.setKorisnik(korisnik);
                voditeljService.save(voditelj);
                return korisnikService.save(korisnik);
            }
            else throw new RequestDeniedException("Voditelj je vec potvrden");

        } else{

            throw new UserNotFoundException("Korisnik s tim id-om "+ id +" ne postoji");

        }

    }

    @PutMapping("/decline/{id}")
    public Korisnik declineUser(@PathVariable("id") Long id) {

        Optional<Korisnik> optionalVoditelj = korisnikService.findById(id);

        if(optionalVoditelj.isPresent()) {
            Korisnik korisnik = optionalVoditelj.get();
            if(korisnik.getUloga() != Uloga.VODITELJ){
                throw new RequestDeniedException("Taj korisnik nije voditelj pa ga ne mozete odbiti");
            }
            else if(korisnik.getPotvrden() == null || korisnik.getPotvrden()) {
                korisnik.setPotvrden(false);
                System.out.println("Korisnik je odbijen");
                return korisnikService.save(korisnik);
            }
            else throw new RequestDeniedException("Voditelj je vec odbijen");

        } else{

            throw new UserNotFoundException("Korisnik s tim id-om "+ id +" ne postoji");

        }

    }
    //@DeleteMapping("/delete/{userId}")
    //public ResponseEntity<String> deleteUser(@PathVariable("userId") Long id){
    //    try{
    //        //korisnikRepository.deleteById(id);
    //        voditeljService.deleteById(id);
    //        return ResponseEntity.ok("User deleted successfully");
    //    }catch (UsernameNotFoundException e){
    //        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    //    }catch (Exception e){
    //        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user: " + e.getMessage());
    //    }
    //}


/*
    @PutMapping("/update/{id}")
    // ovdje bi trebao napraviti DTO
    public Korisnik updateKorisnik(@PathVariable("id") Long id, @RequestBody PersonalInformation requestKorisnik) {

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
            if(requestKorisnik.getUloga()==uloga.VODITELJ){
                var voditelj = new Voditelj();
                voditelj.setKorisnik(requestKorisnik);
                voditeljRepository.save(voditelj);
                return voditelj.getKorisnik();
            }else{
                return korisnikRepository.save(requestKorisnik);
            }


        }else
            throw new RequestDeniedException("Taj korisnik ne postoji u bazi (nema id-a)");
    }

 */




}


