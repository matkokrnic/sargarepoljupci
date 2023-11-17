package com.progi.sargarepoljupci.Models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Korisnik implements UserDetails {


    @Id
//    @SequenceGenerator(
//            name = "sequence",
//            sequenceName = "sequence",
//            allocationSize = 1
//    )
    @GeneratedValue
    private Long korisnikID;

    private String korisnickoIme;
    private String lozinka;

    @Email
    private String email;
    //todo provjerit jel valjan oblik?


    private String iban;
    //todo ogranicen broj znakova, prva dva slova


    private String ime;
    private String prezime;

    @Enumerated(EnumType.STRING)
    private Role rola;

    private Boolean enabled;
    private Boolean locked;


    private String slikaOsobne;

//    public Korisnik(String korisnickoIme,
//                    String lozinka,
//                    String email,
//                    String iban,
//                    String ime,
//                    String prezime,
//                    Role rola,
//                    Boolean enabled,
//                    Boolean locked,
//                    String slikaOsobne) {
//        this.korisnickoIme = korisnickoIme;
//        this.lozinka = lozinka;
//        this.email = email;
//        this.iban = iban;
//        this.ime = ime;
//        this.prezime = prezime;
//        this.rola = rola;
//        this.enabled = enabled;
//        this.locked = locked;
//        this.slikaOsobne = slikaOsobne;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(rola.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return lozinka;
    }

    @Override
    public String getUsername() {
        return korisnickoIme;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
    //todo koji kurac





}
