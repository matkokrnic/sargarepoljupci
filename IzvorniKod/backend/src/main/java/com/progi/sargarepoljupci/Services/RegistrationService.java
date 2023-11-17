package com.progi.sargarepoljupci.Services;

import com.progi.sargarepoljupci.Models.ConfirmationToken;
import com.progi.sargarepoljupci.Models.EmailSender;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegistrationService {

    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final KorisnikService korisnikService;

    public RegistrationService(ConfirmationTokenService confirmationTokenService, EmailSender emailSender, KorisnikService korisnikService) {
        this.confirmationTokenService = confirmationTokenService;
        this.emailSender = emailSender;
        this.korisnikService = korisnikService;
    }


    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        korisnikService.enableKorisnik(
                confirmationToken.getKorisnik().getEmail());
        return "confirmed";
    }

}
