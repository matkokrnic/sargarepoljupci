package com.progi.sargarepoljupci.Services;

import com.progi.sargarepoljupci.DTO.Request.PersonalInformationRequest;
import com.progi.sargarepoljupci.DTO.RegistrationDTO;
import com.progi.sargarepoljupci.Models.Korisnik;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public interface KorisnikServiceInterface {

    public boolean doesKorisnikExistByEmail(String email);
    //public boolean doesKorisnikExistByUsername(String email);
    public Korisnik createKorisnik(RegistrationDTO korisnik, MultipartFile photo) throws IOException, SQLException;

    //Korisnik updateKorisnik(Korisnik student);


    //Optional<Korisnik> findByVerifikacijaToken(String verifikacijaToken);

    void  updateKorisnik(Long userId, PersonalInformationRequest userRequest) throws SQLException, IOException;

    List<Korisnik> findByVoditeljNotApproved();
    Optional<Korisnik> findById(Long aLong);





}
