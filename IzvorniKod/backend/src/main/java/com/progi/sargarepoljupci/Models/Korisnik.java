package com.progi.sargarepoljupci.Models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
public class Korisnik {


    @Id
    @GeneratedValue
    private Long korisnikID;

    private String korisnickoIme;
    private String lozinka;

    @Email
    private String email;


    private String iban;


    private String ime;
    private String prezime;


    private String slikaOsobne;




}
