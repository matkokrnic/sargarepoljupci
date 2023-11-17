package com.progi.sargarepoljupci.Services;

import com.progi.sargarepoljupci.Models.korisnik;
import java.util.List;
import java.util.Optional;


public interface korisnikServiceInterface {

    public boolean doesKorisnikExistByEmail(String email);
    public boolean doesKorisnikExistByUsername(String email);
    public void createKorisnik(korisnik korisnik);

    korisnik updateKorisnik(korisnik student);


    Optional<korisnik> findByVerifikacijaToken(String verifikacijaToken);

    List<korisnik> findByVoditeljNotApproved();
    Optional<korisnik> findById(Long aLong);





}
