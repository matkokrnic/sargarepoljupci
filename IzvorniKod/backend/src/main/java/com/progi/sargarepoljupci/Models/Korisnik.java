package com.progi.sargarepoljupci.Models;


import com.progi.sargarepoljupci.DTO.registrationDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;


@Data
@Entity
public class Korisnik {

    // ako imamo mogucnost promjene korisnickogImena nema smisla da je
    // PK, mislim da je lakse staviti samo generiran id
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true)
    private String korisnickoIme;

    private String lozinka;


    @Column(unique=true)
    @Email
    private String email;

    private String iban;


    private String ime;
    private String prezime;

    @NotNull
    private String slikaOsobne;

    private uloga uloga;

// potvrden voditelj
    private Boolean potvrden;

    private Boolean verificiran;

    private String verifikacijaToken;

    private double WalletBalance;

    public Korisnik() {
    }

    public Korisnik(registrationDTO dto) {
        this.korisnickoIme = dto.getKorisnickoIme();
        this.lozinka = dto.getLozinka();
        this.email = dto.getEmail();
        this.iban = dto.getIban();
        this.ime = dto.getIme();
        this.prezime = dto.getPrezime();
        this.slikaOsobne = dto.getSlikaOsobne();
        this.uloga = dto.getUloga();
        this.potvrden = false; // Default value for potvrden
        this.verificiran = false; // Default value for verificiran
        this.verifikacijaToken = null; // Default value for verifikacijaToken
        this.WalletBalance = 0.;
    }



}
