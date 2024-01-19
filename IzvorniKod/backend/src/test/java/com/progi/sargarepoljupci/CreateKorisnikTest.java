package com.progi.sargarepoljupci;

import com.progi.sargarepoljupci.DTO.RegistrationDTO;
import com.progi.sargarepoljupci.DTO.Uloga;
import com.progi.sargarepoljupci.Exceptions.RequestDeniedException;
import com.progi.sargarepoljupci.Models.Korisnik;
import com.progi.sargarepoljupci.Repository.KorisnikRepository;
import com.progi.sargarepoljupci.Services.KorisnikService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CreateKorisnikTest {

    @Mock
    private KorisnikRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private KorisnikService korisnikService;

    private RegistrationDTO registrationDTO;

    private MockMultipartFile photo;

    @BeforeEach
    void setUp() throws Exception{
        registrationDTO = new RegistrationDTO();
        registrationDTO.setKorisnickoIme("john_doe");
        registrationDTO.setLozinka("password123");
        registrationDTO.setEmail("john.doe@example.com");
        registrationDTO.setIban("HR1234567890123456789");
        registrationDTO.setIme("John");
        registrationDTO.setPrezime("Doe");
        registrationDTO.setUloga(Uloga.KLIJENT);
        photo = new MockMultipartFile("photo", "test.jpg", "image/jpeg", "test image".getBytes());
    }

    @Test
    public void createKorisnikSuccess() throws IOException, SQLException {


        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByKorisnickoIme(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(new Korisnik());


        Korisnik result = korisnikService.createKorisnik(registrationDTO, photo);

        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, times(1)).existsByKorisnickoIme(anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(any());


        assertNotNull(result.getVerifikacijaToken(), "Verification token should not be null");

    }

    @Test
    public void EmailAlreadyExists(){

        when(userRepository.existsByEmail(anyString())).thenReturn(true);


        RequestDeniedException exception = assertThrows(RequestDeniedException.class,
                () -> korisnikService.createKorisnik(registrationDTO, photo));


        verify(userRepository, times(1)).existsByEmail(anyString());


        assertEquals("Korisnik s tim emailom vec postoji u bazi podataka", exception.getMessage(),
                "Korisnik s tim emailom vec postoji u bazi podataka");
    }

    @Test
    public void UsernameAlreadyExists() {

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByKorisnickoIme(anyString())).thenReturn(true);

        // Act and Assert
        RequestDeniedException exception = assertThrows(RequestDeniedException.class,
                () -> korisnikService.createKorisnik(registrationDTO, photo));

        // Verify that the userRepository method was called with the correct parameters
        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, times(1)).existsByKorisnickoIme(anyString());

        // Assert the exception message
        assertEquals("Korisnik s tim usernameom vec postoji u bazi podataka", exception.getMessage(),
                "Korisnik s tim usernameom vec postoji u bazi podataka");
    }

    @Test
    public void createKorisnikNullOrEmptyPhoto() {
        MockMultipartFile photoNull = new MockMultipartFile("photo", (byte[]) null);
        MockMultipartFile photoEmpty = new MockMultipartFile("photo", new byte[0]);
        assertThrows(NullPointerException.class,
                () -> korisnikService.createKorisnik(registrationDTO, photoEmpty));
        assertThrows(NullPointerException.class,
                () -> korisnikService.createKorisnik(registrationDTO, photoNull));


        verify(userRepository, never()).save(any());
    }
}
