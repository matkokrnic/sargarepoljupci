package com.progi.sargarepoljupci;

import com.progi.sargarepoljupci.DTO.Request.PersonalInformationRequest;
import com.progi.sargarepoljupci.DTO.Uloga;
import com.progi.sargarepoljupci.Models.Korisnik;
import com.progi.sargarepoljupci.Models.Voditelj;
import com.progi.sargarepoljupci.Repository.KorisnikRepository;
import com.progi.sargarepoljupci.Repository.VoditeljRepository;
import com.progi.sargarepoljupci.Services.KorisnikService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UpdateKorisnikTest {

    @Mock
    private KorisnikRepository userRepository;

    @Mock
    private VoditeljRepository voditeljRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private KorisnikService korisnikService;

    @Test
    public void testUpdateKorisnik() throws SQLException, IOException {
        // Arrange
        Long userId = 1L;
        PersonalInformationRequest userRequest = new PersonalInformationRequest();
        userRequest.setUsername("newUsername");
        userRequest.setFirstName("newFirstName");
        userRequest.setPhoto(new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", "test".getBytes()));
        userRequest.setPassword("password123");

        Korisnik existingUser = new Korisnik();
        existingUser.setId(userId);
        existingUser.setKorisnickoIme("storaKorisnicko");
        existingUser.setIme("staroIme");
        userRequest.setPassword("stara");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Act
        korisnikService.updateKorisnik(userId, userRequest);

        // Verify that the userRepository's save method is called with the updated existingUser
        verify(userRepository).save(argThat(updatedUser ->
                        updatedUser.getId().equals(userId) &&
                                updatedUser.getKorisnickoIme().equals(userRequest.getUsername()) &&
                                updatedUser.getIme().equals(userRequest.getFirstName())
        ));

        // Verify that passwordEncoder.encode is called with the new password
        verify(passwordEncoder).encode(eq(userRequest.getPassword()));

        // Verify that voditeljRepository.save is called if the user's role is VODITELJ
        if (existingUser.getUloga() == Uloga.VODITELJ) {
            verify(voditeljRepository).save(any(Voditelj.class));
        } else {
            // Verify that voditeljRepository.save is not called if the user's role is not VODITELJ
            verify(voditeljRepository, never()).save(any(Voditelj.class));
        }
    }
}

