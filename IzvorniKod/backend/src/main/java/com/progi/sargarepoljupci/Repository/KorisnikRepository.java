package com.progi.sargarepoljupci.Repository;

import com.progi.sargarepoljupci.Models.Korisnik;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {

    boolean existsByEmail(String email);

    Optional<Korisnik> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Korisnik a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableKorisnik(String email);

}

