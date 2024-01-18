package com.progi.sargarepoljupci.Services;

import com.progi.sargarepoljupci.DTO.PersonalInformation;
import com.progi.sargarepoljupci.Exceptions.RequestDeniedException;
import com.progi.sargarepoljupci.Models.Korisnik;
import com.progi.sargarepoljupci.Models.Voditelj;
import com.progi.sargarepoljupci.Models.uloga;
import com.progi.sargarepoljupci.Repository.VoditeljRepository;
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
    private final VoditeljRepository voditeljRepository;


    @Autowired
    public korisnikService(korisnikRepository userRepository, PasswordEncoder passwordEncoder, VoditeljRepository voditeljRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.voditeljRepository = voditeljRepository;
    }

    public boolean doesKorisnikExistByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    /*
    @Override
    public boolean doesKorisnikExistByUsername(String korisnickoIme) {
        return userRepository.existsByKorisnickoIme(korisnickoIme);
    }

     */


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
    public void  updateKorisnik(Long userId, PersonalInformation userRequest)  {

        Korisnik existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RequestDeniedException("User not found"));

        updateNonNullFields(existingUser, userRequest);




        // Check if the password is provided and needs to be updated
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            String encryptedPassword = passwordEncoder.encode(userRequest.getPassword());
            existingUser.setLozinka(encryptedPassword);
        }
        if (existingUser.getUloga() == uloga.VODITELJ) {
            var voditelj = new Voditelj();
            voditelj.setKorisnik(existingUser);
            voditeljRepository.save(voditelj);

        }else {

            userRepository.save(existingUser);
        }
    }

    private void updateNonNullFields(Korisnik existingUser, PersonalInformation userRequest) {
        // Update fields only if they are not null in the request
        if (userRequest.getUsername() != null) {
            existingUser.setKorisnickoIme(userRequest.getUsername());
        }
        if (userRequest.getFirstName() != null) {
            existingUser.setIme(userRequest.getFirstName());
        }
        if (userRequest.getLastName() != null) {
            existingUser.setPrezime(userRequest.getLastName());
        }
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            String encryptedPassword = passwordEncoder.encode(userRequest.getPassword());
            existingUser.setLozinka(encryptedPassword);
        }
        if (userRequest.getPhoto() != null) {
            existingUser.setSlikaOsobne(userRequest.getPhoto());
        }
        if (userRequest.getIban() != null) {
            existingUser.setIban(userRequest.getIban());
        }
        if (userRequest.getEmailAddress() != null) {
            existingUser.setEmail(userRequest.getEmailAddress());
        }
    }




    /**
     *  <p>provjera postoji li vec neki korisnik s tim mailom ili korisnickim imenom u bazi podataka</p>
     *  <p>provjera postoji li korisnik s tim id-em u bazi podataka (ako ne nemamo sta updateati)</p>
     */

    //trebao bih jos dodati provjeru je li URL dobar za sliku...
    /*private void validate(Korisnik korisnik) {

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

     */

    /*
    @Override
    public Optional<Korisnik> findByVerifikacijaToken(String verifikacijaToken) {
        return userRepository.findByVerifikacijaToken(verifikacijaToken);
    }

     */


    @Override
    public List<Korisnik> findByVoditeljNotApproved() {
        return userRepository.findByUlogaIsAndPotvrdenNullOrPotvrdenIsFalseAndVerificiranIsTrue(uloga.VODITELJ);
    }

    @Override
    public Optional<Korisnik> findById(Long aLong) {
        return userRepository.findById(aLong);
    }


}
