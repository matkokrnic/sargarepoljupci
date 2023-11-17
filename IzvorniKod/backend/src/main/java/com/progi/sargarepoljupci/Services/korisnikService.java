package com.progi.sargarepoljupci.Services;

import com.progi.sargarepoljupci.Exceptions.korisnikNotFoundException;
import com.progi.sargarepoljupci.Exceptions.requestDeniedException;
import com.progi.sargarepoljupci.Models.korisnik;
import com.progi.sargarepoljupci.Models.uloga;
import com.progi.sargarepoljupci.Utilities.verificationTokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.core.KotlinReflectionParameterNameDiscoverer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class korisnikService implements korisnikServiceInterface {



    private final com.progi.sargarepoljupci.Repository.korisnikRepository korisnikRepository;
    private final PasswordEncoder passwordEncoder;




    @Autowired
    public korisnikService(com.progi.sargarepoljupci.Repository.korisnikRepository korisnikRepository, PasswordEncoder passwordEncoder) {
        this.korisnikRepository = korisnikRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean doesKorisnikExistByEmail(String email){
        return korisnikRepository.existsByEmail(email);
    }

    @Override
    public boolean doesKorisnikExistByUsername(String korisnickoIme) {
        return korisnikRepository.existsByKorisnickoIme(korisnickoIme);
    }


    //trebao bih pretvoriti URL slike u bytearray
    public void createKorisnik(korisnik korisnik) {

        if(korisnikRepository.existsByEmail(korisnik.getEmail())){
            throw new requestDeniedException("Korisnik s tim emailom vec postoji u bazi podataka");
        }
        if(korisnikRepository.existsByKorisnickoIme(korisnik.getKorisnickoIme())){
            throw new requestDeniedException("Korisnik s tim usernameom vec postoji u bazi podataka");
        }



        // trebamo encodirat lozinku
        log.info(korisnik.getLozinka());
        String encodedPassword = passwordEncoder.encode(korisnik.getLozinka());
        korisnik.setLozinka(encodedPassword);
        // trebam postaviti verificationToken za mail
        String verificationToken = verificationTokenGenerator.generateUniqueVerificationToken();
        korisnik.setVerifikacijaToken(verificationToken);




        korisnikRepository.save(korisnik);
    }
    @Override
    public korisnik updateKorisnik(korisnik korisnik)  {

        validate(korisnik);

        return korisnikRepository.save(korisnik);
    }



    /**
     *  <p>provjera postoji li vec neki korisnik s tim mailom ili korisnickim imenom u bazi podataka</p>
     *  <p>provjera postoji li korisnik s tim id-em u bazi podataka (ako ne nemamo sta updateati)</p>
     */

    //trebao bih jos dodati provjeru je li URL dobar za sliku...
    private void validate(korisnik korisnik) {

        if(korisnik == null){
            throw new requestDeniedException("Korisnik ne smije biti null");
        }
        String email = korisnik.getEmail();

        Long id = korisnik.getId();
        if(!korisnikRepository.existsById(id)){
            throw new korisnikNotFoundException("Korisnik ne postoji u bazi podataka");

        }
        Optional<korisnik> tempKorisnik = korisnikRepository.findById(id);
        // postoji sansa da postoji vec neki drugi user sa tim mailom
        // id razlicit, email isti --> error
        if(!korisnikRepository.existsByEmailAndIdNot(korisnik.getEmail(), id)){
            throw new requestDeniedException("Email u koji zelite promijeniti vec postoji u bazi podataka " + korisnik.getEmail());
        }
        // postoji sansa da postoji vec neki drugi user sa tim usernameom
        // analogno za ovaj slucaj id razlicit, username isti --> error
        if(!korisnikRepository.existsByEmailAndIdNot(korisnik.getKorisnickoIme(), id)){
            throw new requestDeniedException("Korisnicko ime u koje zelite promijeniti vec postoji u bazi podataka " + korisnik.getKorisnickoIme());
        }

    }

    @Override
    public Optional<korisnik> findByVerifikacijaToken(String verifikacijaToken) {
        return korisnikRepository.findByVerifikacijaToken(verifikacijaToken);
    }


    @Override
    public List<korisnik> findByVoditeljNotApproved() {
        return korisnikRepository.findByUlogaIsAndPotvrdenNullOrPotvrdenIsFalseAndVerificiranIsTrue(uloga.VODITELJ);
    }

    @Override
    public Optional<korisnik> findById(Long aLong) {
        return korisnikRepository.findById(aLong);
    }


}
