package com.progi.sargarepoljupci.Models;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;


@Data
@Entity
public class korisnik {

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





}
