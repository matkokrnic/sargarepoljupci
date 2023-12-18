package com.progi.sargarepoljupci.DTO;

import com.progi.sargarepoljupci.Models.uloga;
import lombok.Data;

@Data
public class registrationDTO {

    private String korisnickoIme;
    private String lozinka;
    private String email;
    private String iban;
    private String ime;
    private String prezime;
    private String slikaOsobne;
    private uloga Uloga;
}