package com.progi.sargarepoljupci.Models;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Data
public class UserDTO implements UserDetails {

    private String username;
    private String password;
    private List<GrantedAuthority> authorities;


    // PREIMENOVATI U PERSONALUSERDETAILS ILI TAKO NESTO
    public UserDTO(korisnik user) {

        username = user.getKorisnickoIme();
        password = user.getLozinka();
        authorities = List.of(new SimpleGrantedAuthority(user.getUloga().toString()));
        //authorities = Arrays.stream(user.getRoles()
        //                .split(","))
        //        .map(SimpleGrantedAuthority::new)
        //        .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
