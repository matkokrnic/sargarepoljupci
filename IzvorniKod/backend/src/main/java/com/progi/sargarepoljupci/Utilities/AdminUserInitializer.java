package com.progi.sargarepoljupci.Utilities;

import com.progi.sargarepoljupci.Models.Korisnik;
import com.progi.sargarepoljupci.DTO.Uloga;
import com.progi.sargarepoljupci.Repository.KorisnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    private final KorisnikRepository korisnikRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminUserInitializer(KorisnikRepository korisnikRepository, PasswordEncoder passwordEncoder) {
        this.korisnikRepository = korisnikRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws SQLException {
        initializeAdminUser();
    }

    private void initializeAdminUser() throws SQLException {
        if (!korisnikRepository.existsByKorisnickoIme("admin")) {
            Korisnik adminUser = new Korisnik();
            adminUser.setKorisnickoIme("admin");
            adminUser.setLozinka(passwordEncoder.encode("123"));
            byte[] a = {72, 101, 108, 108, 111};
            adminUser.setSlikaOsobne(a);

            adminUser.setUloga(Uloga.ADMIN);

            korisnikRepository.save(adminUser);
            System.out.println("Admin uspjesno inicijaliziran " + adminUser.getKorisnickoIme() + " 123");
        } else if (!korisnikRepository.existsByKorisnickoIme("admin")) {
            Korisnik adminUser = new Korisnik();
            adminUser.setKorisnickoIme("admin");
            adminUser.setLozinka(passwordEncoder.encode("123"));


            adminUser.setUloga(Uloga.ADMIN);

            korisnikRepository.save(adminUser);
            System.out.println("Admin uspjesno inicijaliziran " + adminUser.getKorisnickoIme() + " 123");
        } else
            System.out.println("Admin user already exists. username: admin, password: 123");
    }


}