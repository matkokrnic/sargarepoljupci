package com.progi.sargarepoljupci.Exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequestDeniedException extends RuntimeException{

    public RequestDeniedException() {
    }

    public RequestDeniedException(String message) {
        super(message);
    }

    public RequestDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestDeniedException(Throwable cause) {
        super(cause);
    }
}
