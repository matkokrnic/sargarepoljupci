package com.progi.sargarepoljupci.Services;

import com.progi.sargarepoljupci.Models.ConfirmationToken;
import com.progi.sargarepoljupci.Models.Korisnik;
import com.progi.sargarepoljupci.Repository.KorisnikRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class KorisnikService implements UserDetailsService{



    private final KorisnikRepository korisnikRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;




    @Autowired
    public KorisnikService(KorisnikRepository korisnikRepository, PasswordEncoder passwordEncoder, ConfirmationTokenService confirmationTokenService) {
        this.korisnikRepository = korisnikRepository;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenService = confirmationTokenService;
    }
//    provjeritkasnije

    public boolean doesKorisnikExist(String email){
        return korisnikRepository.existsByEmail(email);
    }

    public String createKorisnik(Korisnik korisnik) {
        log.info(korisnik.getLozinka());
        String encodedPassword = passwordEncoder.encode(korisnik.getLozinka());
        korisnik.setLozinka(encodedPassword);


        korisnikRepository.save(korisnik);


        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                korisnik
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;


    }

    public int enableKorisnik(String email) {
        return korisnikRepository.enableKorisnik(email);
    }

    private final static String USER_NOT_FOUND_MSG = "korisnik s emailom %s nije pronaden";
//    private final KorisnikRepository korisnikRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            return korisnikRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
//        throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, ));
    }


}
