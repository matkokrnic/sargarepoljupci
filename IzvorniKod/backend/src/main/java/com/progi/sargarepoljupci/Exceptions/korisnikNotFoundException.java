package com.progi.sargarepoljupci.Exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class korisnikNotFoundException extends AuthenticationException {

    public korisnikNotFoundException(String message) {
        super(message);
    }

    public korisnikNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }


}
