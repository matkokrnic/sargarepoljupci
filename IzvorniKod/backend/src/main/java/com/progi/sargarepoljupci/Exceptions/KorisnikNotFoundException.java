package com.progi.sargarepoljupci.Exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class KorisnikNotFoundException extends Exception{
    public KorisnikNotFoundException() {
    }

    public KorisnikNotFoundException(String message) {
        super(message);
    }

    public KorisnikNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public KorisnikNotFoundException(Throwable cause) {
        super(cause);
    }
}
