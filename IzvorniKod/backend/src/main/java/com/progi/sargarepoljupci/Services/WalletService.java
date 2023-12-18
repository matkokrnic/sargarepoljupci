package com.progi.sargarepoljupci.Services;

import com.progi.sargarepoljupci.Models.Korisnik;
import com.progi.sargarepoljupci.Repository.korisnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletService {

    private final korisnikRepository userRepository;
    @Autowired
    public WalletService(korisnikRepository userRepository) {
        this.userRepository = userRepository;
    }



    public boolean depositFunds(Long userId, double amount) {
        var user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            user.setWalletBalance(user.getWalletBalance()+amount);
            return true;
        }
        return false;
    }
}
