package com.progi.sargarepoljupci.Services;

import com.progi.sargarepoljupci.Exceptions.RequestDeniedException;
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

    public void withdrawFunds(Long userId, double amount) {
        Optional<Korisnik> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            Korisnik user = userOptional.get();
            double currentBalance = user.getWalletBalance();

            if (currentBalance >= amount) {
                user.setWalletBalance(currentBalance - amount);
                userRepository.save(user);
            } else {
                throw new RequestDeniedException("Insufficient balance.");
            }
        } else {
            throw new RequestDeniedException("User not found.");
        }
    }


}
