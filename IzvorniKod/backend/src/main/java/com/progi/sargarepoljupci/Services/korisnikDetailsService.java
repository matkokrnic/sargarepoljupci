package com.progi.sargarepoljupci.Services;

import com.progi.sargarepoljupci.DTO.UserDTO;
import com.progi.sargarepoljupci.Repository.korisnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class korisnikDetailsService implements UserDetailsService {


    private final korisnikRepository userRepository;
    @Autowired
    public korisnikDetailsService(korisnikRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByKorisnickoIme(username)
                .map(UserDTO::new)
                .orElseThrow(() -> new UsernameNotFoundException("No user found"));
    }

}
