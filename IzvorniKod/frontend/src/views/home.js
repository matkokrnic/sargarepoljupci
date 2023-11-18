import React from 'react';

// Helmet koristi se za postavljanje meta informacija o stranici
import { Helmet } from 'react-helmet';
// Navbar i Footer su komponente koje koristimo u layout-u
import Navbar from './partials/navbar';
import Footer from './partials/footer';

// Stilovi za naslovnu stranicu
import './home.css';

function Home() {
  // Provjera je li korisnik prijavljen (pretpostavka da je prijavljen ako postoji korisničko ime u localStorage-u)
  let isLoggedIn = true;

  if (localStorage.getItem('username') == null) {
    isLoggedIn = false;
  }

  // Inicijalizacija varijabli koje će sadržavati gumb za registraciju i prijavu
  let registerButton, loginButton;

  // Ako korisnik nije prijavljen, postavi gumb za registraciju i prijavu
  if (!isLoggedIn) {
    registerButton = <a href="/register" className='button-href'><button className="button button-gradient">Pridruži se</button></a>
    loginButton = <a href="/login" className='button-href'><button className="button button-transparent">Prijava</button></a>
  }

  return (
    <div className="page-container mandatory-scroll-snapping">
      {/* Postavljanje naslova stranice */}
      <Helmet>
        <title>SpotPicker | Naslovnica</title>
      </Helmet>

      {/* Uključivanje Navbar komponente */}
      <Navbar />

      {/* Container za sekciju s pozadinom "background-storm" */}
      <div className="section-container background-storm">
        <div className="home-text-content">
          {/* Podnaslov */}
          <span className="home-subtitle">
            izaberite pravo mjesto za vaš automobil
          </span>
          {/* Naslov */}
          <h1 className="home-title">
            <span>Cjelodnevni parking ili po satu,</span><br />
            <span className="text-apricot">Vi birate</span>
          </h1>
          {/* Opis */}
          <span className="home-description">
            Opširna ponuda parkirnih mjesta osigurava savršen odabir za svakoga, <br />
            Pronađite pravi parking u pravo vrijeme, <br /> <br />
            Što čekate, pridružite nam se odmah!
          </span>
          {/* Kontejner za gumbe */}
          <div className="home-button-container">
            {registerButton}
            {loginButton}
          </div>
        </div>
        {/* Container za glavnu sliku */}
        <div className="home-main-image-container">
          <img src="/images/homepic1.jpg" alt="parking" className="home-main-image" />
        </div>
      </div>

      {/* Footer komponenta */}
      <Footer />
    </div>
  );
}

export default Home;
