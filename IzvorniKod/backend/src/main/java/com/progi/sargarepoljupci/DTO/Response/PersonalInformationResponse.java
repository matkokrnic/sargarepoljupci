package com.progi.sargarepoljupci.DTO.Response;

import com.progi.sargarepoljupci.Models.Korisnik;
import lombok.Data;
import org.apache.tomcat.util.codec.binary.Base64;

@Data
public class PersonalInformationResponse {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String photo;
    private String iban;
    private String emailAddress;

    public PersonalInformationResponse(Korisnik korisnik) {
        this.username = korisnik.getKorisnickoIme();
        this.firstName = korisnik.getIme();
        this.lastName = korisnik.getPrezime();
        byte[] photoBytes = korisnik.getSlikaOsobne();

        this.photo = photoBytes != null ? Base64.encodeBase64String(photoBytes) : null;

        this.iban = korisnik.getIban();
        this.emailAddress = korisnik.getEmail();
        this.id = korisnik.getId();
    }

    public PersonalInformationResponse(Long id, String username, String firstName, String lastName, String photo, String iban, String emailAddress) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photo = photo;
        this.iban = iban;
        this.emailAddress = emailAddress;
    }

    public PersonalInformationResponse() {
    }
}

