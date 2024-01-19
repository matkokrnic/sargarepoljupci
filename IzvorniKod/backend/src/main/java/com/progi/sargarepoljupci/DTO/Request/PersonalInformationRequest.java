package com.progi.sargarepoljupci.DTO.Request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PersonalInformationRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private MultipartFile photo;
    private String iban;
    private String emailAddress;

    public PersonalInformationRequest() {
    }

    public PersonalInformationRequest(String username, String password, String firstName, String lastName, MultipartFile photo, String iban, String emailAddress) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photo = photo;
        this.iban = iban;
        this.emailAddress = emailAddress;


    }
}

