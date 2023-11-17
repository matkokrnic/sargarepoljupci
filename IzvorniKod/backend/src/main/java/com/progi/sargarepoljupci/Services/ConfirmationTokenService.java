package com.progi.sargarepoljupci.Services;

import com.progi.sargarepoljupci.Models.ConfirmationToken;
import com.progi.sargarepoljupci.Repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;


    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }

    public void saveConfirmationToken(ConfirmationToken token){
        confirmationTokenRepository.save(token);

    }
}
