import React from 'react'

import { Helmet } from 'react-helmet'
import Navbar from './partials/navbar'
import Footer from './partials/footer'

import './home.css'

function Home() {
    let isLoggedIn = true;


    if (localStorage.getItem("username") == null) {
        isLoggedIn = false;
    }

    if (!isLoggedIn) {
        var registerButton = <a href="/register" className='button-href'><button className="button button-gradient">Pridruži se</button></a>
        var loginButton = <a href="/login" className='button-href'><button className="button button-transparent">Prijava</button></a>
    }
  return (

    <div className="page-container mandatory-scroll-snapping">
      <Helmet>
        <title>SpotPicker | Naslovnica</title>
      </Helmet>

      <Navbar/>

      <div className="section-container background-storm">
          <div className="home-text-content">
            <span className="home-subtitle">
              izaberite pravo mjesto za vaš automobil
            </span>
            <h1 className="home-title">
              <span>Cjelodnevni parking ili po satu,</span><br/>
              <span className="text-apricot">Vi birate</span>
            </h1>
            <span className="home-description">
              Opširna ponuda parkirnih mjesta osigurava savršen odabir za svakoga, <br/>
              Pronađite pravi parking u pravo vrijeme, <br/> <br/>
              Što čekate, pridružite nam se odmah!
            </span>
            <div className="home-button-container">
                {registerButton}
                {loginButton}
            </div>
          </div>
          <div className="home-main-image-container">
            <img src="/images/homepic1.jpg" alt="parking" className="home-main-image"/>
          </div>
      </div>

      <Footer/>
    </div>




  )
}

export default Home;