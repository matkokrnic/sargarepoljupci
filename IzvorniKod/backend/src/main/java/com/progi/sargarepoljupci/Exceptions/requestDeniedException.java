package com.progi.sargarepoljupci.Exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class requestDeniedException extends RuntimeException{

    public requestDeniedException() {
    }

    public requestDeniedException(String message) {
        super(message);
    }

    public requestDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public requestDeniedException(Throwable cause) {
        super(cause);
    }
}
