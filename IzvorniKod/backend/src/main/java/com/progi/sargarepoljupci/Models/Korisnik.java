package com.progi.sargarepoljupci.Models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
@Entity
public class Korisnik {


    @Id
    @GeneratedValue
    private Long KorisnikID;

    private String KorisnickoIme;
    private String Lozinka;

    @Email
    private String email;


    private String IBAN;


    private String Ime;
    private String Prezime;


    private String SlikaOsobne;




}
