package com.progi.sargarepoljupci.Services;

import com.progi.sargarepoljupci.DTO.PersonalInformation;
import com.progi.sargarepoljupci.Models.Korisnik;
import java.util.List;
import java.util.Optional;


public interface korisnikServiceInterface {

    public boolean doesKorisnikExistByEmail(String email);
    //public boolean doesKorisnikExistByUsername(String email);
    public void createKorisnik(Korisnik korisnik);

    //Korisnik updateKorisnik(Korisnik student);


    //Optional<Korisnik> findByVerifikacijaToken(String verifikacijaToken);

    void  updateKorisnik(Long userId, PersonalInformation userRequest);

    List<Korisnik> findByVoditeljNotApproved();
    Optional<Korisnik> findById(Long aLong);





}
