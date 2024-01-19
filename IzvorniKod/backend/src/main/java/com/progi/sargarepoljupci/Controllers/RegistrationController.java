package com.progi.sargarepoljupci.Controllers;

import com.progi.sargarepoljupci.DTO.RegistrationDTO;
import com.progi.sargarepoljupci.Exceptions.RequestDeniedException;
import com.progi.sargarepoljupci.Models.Korisnik;
import com.progi.sargarepoljupci.DTO.Uloga;
import com.progi.sargarepoljupci.Services.KorisnikService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@RestController
@RequestMapping("/api/registration")
@Slf4j
public class RegistrationController {

    private final KorisnikService korisnikService;
    private final KorisnikService repo;


    private final JavaMailSender mailSender;
    @Autowired
    public RegistrationController(KorisnikService korisnikService, KorisnikService repo, JavaMailSender mailSender) {
        this.korisnikService = korisnikService;

        this.repo = repo;
        this.mailSender = mailSender;
    }

    @PostMapping
    public ResponseEntity<?> registerKorisnik(

            @RequestParam("photo") MultipartFile photo,
            @RequestParam("korisnickoIme") String korisnickoIme,
            @RequestParam("lozinka") String lozinka,
            @RequestParam("email") String email,
            @RequestParam("iban") String iban,
            @RequestParam("ime") String ime,
            @RequestParam("prezime") String prezime,
            @RequestParam("uloga") int uloga, HttpServletRequest httpServletRequest) throws MessagingException, SQLException, IOException {

        Uloga uloga1 = Uloga.KLIJENT;
        switch (uloga){
            case 1:
                uloga1 = Uloga.VODITELJ;
                break;
            case 2:
                uloga1 = Uloga.ADMIN;
        }
        var registerDTO = new RegistrationDTO(korisnickoIme,lozinka, email, iban,ime,prezime, uloga1);

        if (korisnikService.doesKorisnikExistByEmail(registerDTO.getEmail())) {
            //ResponseObject responseObject = new ResponseObject();
            //responseObject.setMessage("Email vec postoji");
            //responseObject.setStatus(HttpStatus.BAD_REQUEST.value());
            //return responseObject;
            return ResponseEntity.badRequest().body("Email vec postoji");

        } else {

            log.info(String.valueOf(registerDTO));
            //var user = new Korisnik(registerDTO);
            var user = korisnikService.createKorisnik(registerDTO, photo);
            String url = getSiteURL(httpServletRequest);
            System.out.println(url);
            sendHtmlEmail(user, url);
            log.info(String.valueOf(ResponseEntity.status(HttpStatus.CREATED).body("Stvorili smo korisnika " + registerDTO + "\n Provjeriti mail za verifikaciju")));
            return ResponseEntity.ok(registerDTO);
        }
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }


    private boolean verify(String verificationCode) {
        Optional<Korisnik> korisnik = repo.findByVerifikacijaToken(verificationCode);
        if(korisnik.isEmpty()) {
            return false;
        } else {
            if(korisnik.get().getVerificiran()!=null && korisnik.get().getVerificiran()){
                throw new RequestDeniedException("Korisnik je vec verificiran");
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



    public void sendHtmlEmail(Korisnik korisnik, String siteURL) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(new InternetAddress("sargarepoljupci@gmail.com"));
        message.setRecipients(MimeMessage.RecipientType.TO, korisnik.getEmail());
        message.setSubject("Potvrda za verifikaciju");

        String htmlContent = "Kliknite na link kako biste zavrsili s registracijom:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>";
        String verifyURL = siteURL + "/api/registration/verify?code=" + korisnik.getVerifikacijaToken();
        System.out.println("verifikacijaURL "+ verifyURL);
        System.out.println(htmlContent);
        htmlContent = htmlContent.replace("[[URL]]", verifyURL);
        System.out.println(htmlContent);
        message.setContent(htmlContent, "text/html;");
        System.out.println(message);

        mailSender.send(message);
    }


}

