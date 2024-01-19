package com.progi.sargarepoljupci.DTO;

import lombok.Data;

@Data
public class RegistrationDTO {

    private String korisnickoIme;
    private String lozinka;
    private String email;
    private String iban;
    private String ime;
    private String prezime;
    private Uloga uloga;

    public RegistrationDTO(String korisnickoIme, String lozinka, String email, String iban, String ime, String prezime, Uloga uloga) {
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
        this.email = email;
        this.iban = iban;
        this.ime = ime;
        this.prezime = prezime;
        this.uloga = uloga;
    }

    public RegistrationDTO() {
    }

}
