package com.progi.sargarepoljupci.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class korisnikNotApprovedException extends RuntimeException {
    public korisnikNotApprovedException() {
    }

    public korisnikNotApprovedException(String message) {
        super(message);
    }
}
