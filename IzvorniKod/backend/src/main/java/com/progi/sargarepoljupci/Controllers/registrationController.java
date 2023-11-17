package com.progi.sargarepoljupci.Controllers;

import com.progi.sargarepoljupci.Exceptions.requestDeniedException;
import com.progi.sargarepoljupci.Models.korisnik;
import com.progi.sargarepoljupci.Repository.korisnikRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/registration")
@Slf4j
public class registrationController {

    private final com.progi.sargarepoljupci.Services.korisnikService korisnikService;
    private final korisnikRepository repo;


    private final JavaMailSender mailSender;
    @Autowired
    public registrationController(com.progi.sargarepoljupci.Services.korisnikService korisnikService, korisnikRepository repo, JavaMailSender mailSender) {
        this.korisnikService = korisnikService;

        this.repo = repo;
        this.mailSender = mailSender;
    }

    @PostMapping
    public ResponseEntity<String> registerKorisnik(@RequestBody korisnik korisnik) throws MessagingException {
//        return new ResponseEntity<>("OK", HttpStatus.OK);

        if (korisnikService.doesKorisnikExistByEmail(korisnik.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email vec postoji.");

        } else {

            log.info(String.valueOf(korisnik));
            korisnikService.createKorisnik(korisnik);

            sendHtmlEmail(korisnik, "localhost:8080");


            return ResponseEntity.status(HttpStatus.CREATED).body("Stvorili smo korisnika " + korisnik + "\n Provjeriti mail za verifikaciju");
        }
    }


    private boolean verify(String verificationCode) {
        Optional<korisnik> korisnik = repo.findByVerifikacijaToken(verificationCode);
        if(korisnik.isEmpty()) {
            return false;
        } else {
            if(korisnik.get().getVerificiran()!=null && korisnik.get().getVerificiran()){
                throw new requestDeniedException("Korisnik je vec verificiran");
            }

            korisnik.get().setVerificiran(true);
            repo.save(korisnik.get());
            System.out.println("Korisnik je verificiran");
            return true;
        }
    }
    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (verify(code)) {


            return "verify_success";
        } else {
            return "verify_fail";
        }
    }



    public void sendHtmlEmail(korisnik korisnik, String siteURL) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(new InternetAddress("sargarepoljupci@gmail.com"));
        message.setRecipients(MimeMessage.RecipientType.TO, korisnik.getEmail());
        message.setSubject("Potvrda za verifikaciju");

        String htmlContent = "Kliknite na link kako biste zavrsili s registracijom:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>";
        String verifyURL = "http://" +siteURL + "/api/registration/verify?code=" + korisnik.getVerifikacijaToken();
        System.out.println("verifikacijaURL "+ verifyURL);
        System.out.println(htmlContent);
        htmlContent = htmlContent.replace("[[URL]]", verifyURL);
        System.out.println(htmlContent);
        message.setContent(htmlContent, "text/html;");
        System.out.println(message);

        mailSender.send(message);
    }


}

