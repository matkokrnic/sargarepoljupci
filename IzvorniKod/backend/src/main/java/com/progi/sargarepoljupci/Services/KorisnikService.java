package com.progi.sargarepoljupci.Services;

import com.progi.sargarepoljupci.Models.Korisnik;
import com.progi.sargarepoljupci.Repository.KorisnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class KorisnikService {

    private final KorisnikRepository korisnikRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public KorisnikService(KorisnikRepository korisnikRepository, PasswordEncoder passwordEncoder) {
        this.korisnikRepository = korisnikRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean doesKorisnikExist(String email){
        return korisnikRepository.existsByEmail(email);
    }

    public void createKorisnik(Korisnik korisnik) {

        String encodedPassword = passwordEncoder.encode(korisnik.getLozinka());
        korisnik.setLozinka(encodedPassword);

        korisnikRepository.save(korisnik);
    }
}
