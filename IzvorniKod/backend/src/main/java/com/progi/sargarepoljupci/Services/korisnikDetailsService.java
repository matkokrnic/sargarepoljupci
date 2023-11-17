package com.progi.sargarepoljupci.Services;

import com.progi.sargarepoljupci.Exceptions.korisnikNotApprovedException;
import com.progi.sargarepoljupci.Exceptions.requestDeniedException;
import com.progi.sargarepoljupci.Models.korisnik;
import com.progi.sargarepoljupci.Models.uloga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;
import java.util.List;
import java.util.Optional;


@Service
public class korisnikDetailsService implements UserDetailsService {

    private final com.progi.sargarepoljupci.Repository.korisnikRepository korisnikRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public korisnikDetailsService(com.progi.sargarepoljupci.Repository.korisnikRepository korisnikRepository, PasswordEncoder passwordEncoder) {
        this.korisnikRepository = korisnikRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException  {
        if(username.equals("admin"))return new User(
                "admin",
                "123",
                commaSeparatedStringToAuthorityList("ROLE_ADMIN")
        );


        Optional<korisnik> optKorisnik = korisnikRepository.findByKorisnickoIme(username);


        if (optKorisnik.isEmpty()) {
            throw new UsernameNotFoundException("Korisnik s imenom: " + username + " ne postoji");
        }
        korisnik korisnik = optKorisnik.get();

        if(korisnik.getUloga()== uloga.VODITELJ && !korisnik.getPotvrden()){
            throw new korisnikNotApprovedException("Voditelj nije potvrden");
        }


        return new User(username, korisnik.getLozinka(), authorities(username));


    }



    private List<GrantedAuthority> authorities(String username) throws UsernameNotFoundException {


        if(korisnikRepository.findByKorisnickoIme(username).isEmpty()){
            throw new UsernameNotFoundException("Korisnik s tim imenom nije pronaden");
        }

        korisnik korisnik = korisnikRepository.findByKorisnickoIme(username).get();
        uloga role = korisnik.getUloga();

        if (korisnik.getVerificiran()==null){
            throw new requestDeniedException("Korisnik se nije verificirao mailom");
        }
        switch (role) {
            case VODITELJ: //voditelj
            if(korisnik.getPotvrden())
                return   commaSeparatedStringToAuthorityList("ROLE_MANAGER");
            case KLIJENT://klijent
            return    commaSeparatedStringToAuthorityList("ROLE_USER");
            default: throw new requestDeniedException("Mora biti neki role");
        }
    }

}
