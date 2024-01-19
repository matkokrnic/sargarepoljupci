package com.progi.sargarepoljupci;

public class KorisnikTest {
/*
    private Korisnik korisnik;

    @BeforeEach
    public void setUp() {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setKorisnickoIme("testUser");
        registrationDTO.setLozinka("password");
        registrationDTO.setEmail("test@example.com");
        registrationDTO.setIban("IBAN123");
        registrationDTO.setIme("John");
        registrationDTO.setPrezime("Doe");
        registrationDTO.setUloga(Uloga.KLIJENT);

        korisnik = new Korisnik(registrationDTO, new byte[0]);
    }

    @Test
    public void testDefaultValues() {
        assertFalse(korisnik.getPotvrden());
        assertFalse(korisnik.getVerificiran());
        assertNull(korisnik.getVerifikacijaToken());
        assertEquals(0.0, korisnik.getWalletBalance());
    }

    @Test
    public void testConstructorWithDTO() {
        assertEquals("testUser", korisnik.getKorisnickoIme());
        assertEquals("password", korisnik.getLozinka());
        assertEquals("test@example.com", korisnik.getEmail());
        assertEquals("IBAN123", korisnik.getIban());
        assertEquals("John", korisnik.getIme());
        assertEquals("Doe", korisnik.getPrezime());
        assertEquals(Uloga.KLIJENT, korisnik.getUloga());
    }

    @Test
    public void testSettingZeroLengthPhotoThrowsException() {
        korisnik = new Korisnik();

        assertThrows(IllegalArgumentException.class, () -> {
            korisnik.setSlikaOsobne(new byte[0]);
        });
    }

    @Test
    public void testSettingUsernameToNullThrowsException() {
        korisnik = new Korisnik();
        assertThrows(Exception.class, () -> korisnik.setKorisnickoIme(null));
    }

    @Test
    public void testSettingPasswordToNullThrowsException() {
        korisnik = new Korisnik();
        assertThrows(NullPointerException.class, () -> korisnik.setLozinka(null));
    }

    @Test
    public void testSettingEmailToNullThrowsException() {
        korisnik = new Korisnik();
        assertThrows(NullPointerException.class, () -> korisnik.setEmail(null));
    }

    @Test
    public void testSettingIbanToNullThrowsException() {
        korisnik = new Korisnik();
        assertThrows(NullPointerException.class, () -> korisnik.setIban(null));
    }

    @Test
    public void testSettingImeToNullThrowsException() {
        korisnik = new Korisnik();
        assertThrows(NullPointerException.class, () -> korisnik.setIme(null));
    }

    @Test
    public void testSettingPrezimeToNullThrowsException() {
        korisnik = new Korisnik();
        assertThrows(NullPointerException.class, () -> korisnik.setPrezime(null));
    }


    @Test
    public void testSetAndGetId() {
        korisnik.setId(1L);
        assertEquals(1L, korisnik.getId());
    }

    @Test
    public void testSetAndGetData() {
        korisnik.setPotvrden(true);
        korisnik.setVerificiran(true);
        korisnik.setVerifikacijaToken("token123");
        korisnik.setWalletBalance(100.0);

        assertTrue(korisnik.getPotvrden());
        assertTrue(korisnik.getVerificiran());
        assertEquals("token123", korisnik.getVerifikacijaToken());
        assertEquals(100.0, korisnik.getWalletBalance());
    }


*/
}


