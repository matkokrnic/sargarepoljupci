/*

package com.progi.sargarepoljupci.Utilities;

import com.progi.sargarepoljupci.Models.korisnik;
import com.progi.sargarepoljupci.Models.uloga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class adminUserInitializer implements CommandLineRunner {

    private final com.progi.sargarepoljupci.Repository.korisnikRepository korisnikRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public adminUserInitializer(com.progi.sargarepoljupci.Repository.korisnikRepository korisnikRepository, PasswordEncoder passwordEncoder) {
        this.korisnikRepository = korisnikRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeAdminUser();
    }

    private void initializeAdminUser() {
        if (!korisnikRepository.existsByKorisnickoIme("admin")) {
            korisnik adminUser = new korisnik();
            adminUser.setKorisnickoIme("admin");
            adminUser.setLozinka(passwordEncoder.encode("123"));


            adminUser.setUloga(uloga.ADMIN);

            korisnikRepository.save(adminUser);
            System.out.println("Admin uspjesno inicijaliziran " + adminUser.getKorisnickoIme() + " 123");
        } else if (!korisnikRepository.existsByKorisnickoIme("admin")) {
            korisnik adminUser = new korisnik();
            adminUser.setKorisnickoIme("admin");
            adminUser.setLozinka(passwordEncoder.encode("123"));


            adminUser.setUloga(uloga.ADMIN);

            korisnikRepository.save(adminUser);
            System.out.println("Admin uspjesno inicijaliziran " + adminUser.getKorisnickoIme() + " 123");
        } else
            System.out.println("Admin user already exists.");
        }


}
*/