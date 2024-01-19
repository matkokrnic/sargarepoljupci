package com.progi.sargarepoljupci.Security.JWT2;

import lombok.Data;

@Data
public class JWTAuthenticationRequest {

    private String username;
    private String password;
}
