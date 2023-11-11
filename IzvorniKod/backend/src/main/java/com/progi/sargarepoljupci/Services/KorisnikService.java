package com.progi.sargarepoljupci.Services;

import com.progi.sargarepoljupci.Models.Korisnik;
import com.progi.sargarepoljupci.Repository.KorisnikRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class KorisnikService {

    private final KorisnikRepository korisnikRepository;
    private final BCryptPasswordEncoder encoder;

    public KorisnikService(KorisnikRepository korisnikRepository, BCryptPasswordEncoder encoder) {
        this.korisnikRepository = korisnikRepository;
        this.encoder = encoder;
    }

    public boolean doesKorisnikExist(String email){
        return korisnikRepository.existsByEmail(email);
    }

    public void createUser(Korisnik korisnik) {

        String encodedPassword = encoder.encode(korisnik.getLozinka());
        korisnik.setLozinka(encodedPassword);

        korisnikRepository.save(korisnik);
    }
}
