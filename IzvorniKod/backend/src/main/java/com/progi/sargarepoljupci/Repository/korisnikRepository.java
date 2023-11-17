package com.progi.sargarepoljupci.Repository;

import com.progi.sargarepoljupci.Models.korisnik;
import com.progi.sargarepoljupci.Models.uloga;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface korisnikRepository extends JpaRepository<korisnik, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByKorisnickoImeAndIdNot(String korisnickoIme, Long id);

    boolean existsById(@Nonnull Long id);
    boolean existsByKorisnickoIme(String korisnickoIme);
    Optional<korisnik> findByEmail(String email);
    Optional<korisnik> findByKorisnickoIme(String korisnickoIme);

    Optional<korisnik> findByVerifikacijaToken(String verificationCode);


    //List<korisnik> findByUlogaIsAndPotvrdenFalse(uloga uloga);
    List<korisnik> findByUlogaIsAndPotvrdenNullOrPotvrdenIsFalseAndVerificiranIsTrue(uloga uloga);

    boolean existsByEmailAndIdIsNot(@Email String email, Long id);

    @Override
    @Nonnull
    Optional<korisnik> findById(@Nonnull Long aLong);

    boolean existsByKorisnickoImeAndIdIsNot(String korisnickoIme, Long id);
}
