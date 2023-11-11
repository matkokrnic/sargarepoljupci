package com.progi.sargarepoljupci.Repository;

import com.progi.sargarepoljupci.Models.Korisnik;
import jakarta.validation.constraints.Email;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {

    boolean existsByEmail(String email);

    Optional<Korisnik> findByEmail(String email);

}
