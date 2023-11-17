// Funkcija koja definira Navbar komponentu
function Navbar() {
  // Provjera je li korisnik prijavljen (pretpostavka da je prijavljen ako postoji korisničko ime u localStorage-u)
  let isLoggedIn = true;

  if (localStorage.getItem("username") == null) {
    isLoggedIn = false;
  }

  // Funkcija za odjavu korisnika
  function logout() {
    localStorage.removeItem("username");
    // Preusmjeravanje korisnika na početnu stranicu nakon odjave
    window.location.href = "/";
  }

  // Inicijalizacija varijabli koje će sadržavati elemente ovisno o tome je li korisnik prijavljen
  let welcomeText, logoutButton, loginButton, registerButton;

  // Ako je korisnik prijavljen, postavi tekst dobrodošlice i gumb za odjavu
  if (isLoggedIn) {
    welcomeText = <span className="welcome-text">Pozdrav, {localStorage.getItem("username")}</span>;
    logoutButton = <button className="button-secondary button" onClick={logout}>Odjava</button>;
  } else {
    // Ako korisnik nije prijavljen, postavi gumbe za prijavu i registraciju
    loginButton = <a href="/login"><button className="button-secondary button">Prijava</button></a>;
    registerButton = <a href="/register"><button className="button button-primary">Pridruži se</button></a>;
  }

  // JSX za prikaz navbara
  return (
    <div className="navbar-container">
      {/* Logo i naziv aplikacije */}
      <div className="navbar-logo-container">
      <a href="/"><img src="/images/carlogo.png" className="navbar-logo" alt="logo" /></a>
      <a href="/"><span className="navbar-logo-text" alt="logo">SpotPicker</span></a>
      </div>

      {/* Navigacijske poveznice */}
      <div className="navbar-links">
        {welcomeText}
        <a href="/" className="navbar-text navbar-link">Kontakt</a>
        <a href="/" className="navbar-text navbar-link">Karta</a>
        {logoutButton}
        {loginButton}
        {registerButton}
      </div>
    </div>
  );
}

// Izvoz Navbar komponente kako bi se mogla koristiti u drugim dijelovima aplikacije
export default Navbar;