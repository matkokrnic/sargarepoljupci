package com.progi.sargarepoljupci.Services;

import com.progi.sargarepoljupci.Exceptions.UserNotFoundException;
import com.progi.sargarepoljupci.Exceptions.RequestDeniedException;
import com.progi.sargarepoljupci.Models.Korisnik;
import com.progi.sargarepoljupci.Models.uloga;
import com.progi.sargarepoljupci.Repository.korisnikRepository;
import com.progi.sargarepoljupci.Utilities.VerificationTokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class korisnikService implements korisnikServiceInterface {

    private final korisnikRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    @Autowired
    public korisnikService(com.progi.sargarepoljupci.Repository.korisnikRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean doesKorisnikExistByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean doesKorisnikExistByUsername(String korisnickoIme) {
        return userRepository.existsByKorisnickoIme(korisnickoIme);
    }


    //trebao bih pretvoriti URL slike u bytearray
    public void createKorisnik(Korisnik korisnik) {

        if(userRepository.existsByEmail(korisnik.getEmail())){
            throw new RequestDeniedException("Korisnik s tim emailom vec postoji u bazi podataka");
        }
        if(userRepository.existsByKorisnickoIme(korisnik.getKorisnickoIme())){
            throw new RequestDeniedException("Korisnik s tim usernameom vec postoji u bazi podataka");
        }



        // trebamo encodirat lozinku
        log.info(korisnik.getLozinka());
        String encodedPassword = passwordEncoder.encode(korisnik.getLozinka());
        korisnik.setLozinka(encodedPassword);
        // trebam postaviti verificationToken za mail
        String verificationToken = VerificationTokenGenerator.generateUniqueVerificationToken();
        korisnik.setVerifikacijaToken(verificationToken);




        userRepository.save(korisnik);
    }
    @Override
    public Korisnik updateKorisnik(Korisnik korisnik)  {

        validate(korisnik);

        return userRepository.save(korisnik);
    }



    /**
     *  <p>provjera postoji li vec neki korisnik s tim mailom ili korisnickim imenom u bazi podataka</p>
     *  <p>provjera postoji li korisnik s tim id-em u bazi podataka (ako ne nemamo sta updateati)</p>
     */

    //trebao bih jos dodati provjeru je li URL dobar za sliku...
    private void validate(Korisnik korisnik) {

        if(korisnik == null){
            throw new RequestDeniedException("Korisnik ne smije biti null");
        }
        String email = korisnik.getEmail();

        Long id = korisnik.getId();
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException("Korisnik ne postoji u bazi podataka");

        }
        Optional<Korisnik> tempKorisnik = userRepository.findById(id);
        // postoji sansa da postoji vec neki drugi user sa tim mailom
        // id razlicit, email isti --> error
        if(!userRepository.existsByEmailAndIdNot(korisnik.getEmail(), id)){
            throw new RequestDeniedException("Email u koji zelite promijeniti vec postoji u bazi podataka " + korisnik.getEmail());
        }
        // postoji sansa da postoji vec neki drugi user sa tim usernameom
        // analogno za ovaj slucaj id razlicit, username isti --> error
        if(!userRepository.existsByEmailAndIdNot(korisnik.getKorisnickoIme(), id)){
            throw new RequestDeniedException("Korisnicko ime u koje zelite promijeniti vec postoji u bazi podataka " + korisnik.getKorisnickoIme());
        }

    }

    @Override
    public Optional<Korisnik> findByVerifikacijaToken(String verifikacijaToken) {
        return userRepository.findByVerifikacijaToken(verifikacijaToken);
    }


    @Override
    public List<Korisnik> findByVoditeljNotApproved() {
        return userRepository.findByUlogaIsAndPotvrdenNullOrPotvrdenIsFalseAndVerificiranIsTrue(uloga.VODITELJ);
    }

    @Override
    public Optional<Korisnik> findById(Long aLong) {
        return userRepository.findById(aLong);
    }


}
