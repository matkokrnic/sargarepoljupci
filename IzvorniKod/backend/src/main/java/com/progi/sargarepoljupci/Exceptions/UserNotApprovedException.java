package com.progi.sargarepoljupci.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserNotApprovedException extends RuntimeException {
    public UserNotApprovedException() {
    }

    public UserNotApprovedException(String message) {
        super(message);
    }
}
