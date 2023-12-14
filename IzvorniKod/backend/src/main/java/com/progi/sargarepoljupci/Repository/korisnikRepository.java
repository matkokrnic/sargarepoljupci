package com.progi.sargarepoljupci.Repository;

import com.progi.sargarepoljupci.Models.Korisnik;
import com.progi.sargarepoljupci.Models.uloga;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface korisnikRepository extends JpaRepository<Korisnik, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByKorisnickoImeAndIdNot(String korisnickoIme, Long id);

    boolean existsById(@Nonnull Long id);
    boolean existsByKorisnickoIme(String korisnickoIme);
    Optional<Korisnik> findByEmail(String email);
    Optional<Korisnik> findByKorisnickoIme(String korisnickoIme);

    Optional<Korisnik> findByVerifikacijaToken(String verificationCode);


    //List<korisnik> findByUlogaIsAndPotvrdenFalse(uloga uloga);
    List<Korisnik> findByUlogaIsAndPotvrdenNullOrPotvrdenIsFalseAndVerificiranIsTrue(uloga uloga);
    boolean existsByEmailAndIdIsNot(@Email String email, Long id);

    @Override
    @Nonnull
    Optional<Korisnik> findById(@Nonnull Long aLong);

    boolean existsByKorisnickoImeAndIdIsNot(String korisnickoIme, Long id);
}
