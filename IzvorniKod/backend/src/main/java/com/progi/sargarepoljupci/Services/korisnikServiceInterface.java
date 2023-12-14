package com.progi.sargarepoljupci.Services;

import com.progi.sargarepoljupci.Models.Korisnik;
import java.util.List;
import java.util.Optional;


public interface korisnikServiceInterface {

    public boolean doesKorisnikExistByEmail(String email);
    public boolean doesKorisnikExistByUsername(String email);
    public void createKorisnik(Korisnik korisnik);

    Korisnik updateKorisnik(Korisnik student);


    Optional<Korisnik> findByVerifikacijaToken(String verifikacijaToken);

    List<Korisnik> findByVoditeljNotApproved();
    Optional<Korisnik> findById(Long aLong);





}
