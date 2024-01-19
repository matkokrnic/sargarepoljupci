package com.progi.sargarepoljupci.Services;

import com.progi.sargarepoljupci.DTO.RegistrationDTO;
import com.progi.sargarepoljupci.DTO.Request.PersonalInformationRequest;
import com.progi.sargarepoljupci.DTO.Uloga;
import com.progi.sargarepoljupci.Exceptions.RequestDeniedException;
import com.progi.sargarepoljupci.Models.Korisnik;
import com.progi.sargarepoljupci.Models.Voditelj;
import com.progi.sargarepoljupci.Repository.KorisnikRepository;
import com.progi.sargarepoljupci.Repository.VoditeljRepository;
import com.progi.sargarepoljupci.Utilities.VerificationTokenGenerator;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class KorisnikService implements KorisnikServiceInterface {

    private final KorisnikRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VoditeljRepository voditeljRepository;


    @Autowired
    public KorisnikService(KorisnikRepository userRepository, PasswordEncoder passwordEncoder, VoditeljRepository voditeljRepository) {
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



    public Korisnik createKorisnik(RegistrationDTO registrationDTO, MultipartFile photo) throws IOException, SQLException {

        if(userRepository.existsByEmail(registrationDTO.getEmail())){
            throw new RequestDeniedException("Korisnik s tim emailom vec postoji u bazi podataka");
        }
        if(userRepository.existsByKorisnickoIme(registrationDTO.getKorisnickoIme())){
            throw new RequestDeniedException("Korisnik s tim usernameom vec postoji u bazi podataka");
        }
        if (!photo.isEmpty()){
            byte[] photoBytes = photo.getBytes();
            //Blob photoBlob = new SerialBlob(photoBytes);
            var korisnik = new Korisnik(registrationDTO, photoBytes);
            log.info(registrationDTO.getLozinka());
            String encodedPassword = passwordEncoder.encode(registrationDTO.getLozinka());
            korisnik.setLozinka(encodedPassword);
            // trebam postaviti verificationToken za mail
            String verificationToken = VerificationTokenGenerator.generateUniqueVerificationToken();
            korisnik.setVerifikacijaToken(verificationToken);
            return userRepository.save(korisnik);

        }else
            throw new RequestDeniedException("Picture field can't be empty");


    }


    @Override
    public void  updateKorisnik(Long userId, PersonalInformationRequest userRequest) throws SQLException, IOException {

        Korisnik existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RequestDeniedException("User not found"));

        updateNonNullFields(existingUser, userRequest);


        if (existingUser.getUloga() == Uloga.VODITELJ) {
            var voditelj = new Voditelj();
            voditelj.setKorisnik(existingUser);
            voditeljRepository.save(voditelj);

        }else {

            userRepository.save(existingUser);
        }
    }

    private void updateNonNullFields(Korisnik existingUser, PersonalInformationRequest userRequest) throws IOException, SQLException {
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
        if (!userRequest.getPhoto().isEmpty() && userRequest.getPhoto() != null) {
                byte[] photoBytes = userRequest.getPhoto().getBytes();
                existingUser.setSlikaOsobne(photoBytes);

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
        return userRepository.findByUlogaIsAndPotvrdenNullOrPotvrdenIsFalseAndVerificiranIsTrue(Uloga.VODITELJ);
    }

    @Override
    public Optional<Korisnik> findById(Long aLong) {
        return userRepository.findById(aLong);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByEmailAndIdNot(String email, Long id) {
        return userRepository.existsByEmailAndIdNot(email, id);
    }

    public boolean existsByKorisnickoImeAndIdNot(String korisnickoIme, Long id) {
        return userRepository.existsByKorisnickoImeAndIdNot(korisnickoIme, id);
    }

    public boolean existsById(@Nonnull Long id) {
        return userRepository.existsById(id);
    }

    public boolean existsByKorisnickoIme(String korisnickoIme) {
        return userRepository.existsByKorisnickoIme(korisnickoIme);
    }

    public Optional<Korisnik> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<Korisnik> findByKorisnickoIme(String korisnickoIme) {
        return userRepository.findByKorisnickoIme(korisnickoIme);
    }

    public Optional<Korisnik> findByVerifikacijaToken(String verificationCode) {
        return userRepository.findByVerifikacijaToken(verificationCode);
    }

    public List<Korisnik> findByVerificiranIsTrue() {
        return userRepository.findByVerificiranIsTrue();
    }

    public List<Korisnik> findByUlogaIsAndPotvrdenNullOrPotvrdenIsFalseAndVerificiranIsTrue(Uloga uloga) {
        return userRepository.findByUlogaIsAndPotvrdenNullOrPotvrdenIsFalseAndVerificiranIsTrue(uloga);
    }

    public boolean existsByEmailAndIdIsNot(String email, Long id) {
        return userRepository.existsByEmailAndIdIsNot(email, id);
    }

    public boolean existsByKorisnickoImeAndIdIsNot(String korisnickoIme, Long id) {
        return userRepository.existsByKorisnickoImeAndIdIsNot(korisnickoIme, id);
    }
    public Korisnik save(@Nonnull Korisnik korisnik) {
        // You can add validation or business logic here before saving
        return userRepository.save(korisnik);
    }


}
