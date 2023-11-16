function Navbar(){
    let isLoggedIn = true;


    if (localStorage.getItem("username") == null) {
        isLoggedIn = false;
    }

    function logout() {
        localStorage.removeItem("username");
        window.location.href = "/";
    }

    if (isLoggedIn) {
        var welcomeText = <span className = "welcome-text">Pozdrav, {localStorage.getItem("username")}</span>
        var logoutButton = <button className="button-secondary button" onClick={logout}>Odjava</button>
    } else {
        var loginButton = <a href="/login"><button className="button-secondary button">Prijava</button></a>
        var registerButton = <a href="/register"><button className="button button-primary">Pridruži se</button></a>
    }

    return(
      <div className="navbar-container">
          <div className="navbar-logo-container">
            <img src="/images/carlogo.png" className="navbar-logo"/>
              <span className="navbar-logo-text" alt="logo">SpotPicker</span>
          </div>
          <div className="navbar-links">
              {welcomeText}
            <a href="/" className="navbar-text navbar-link">Kontakt</a>
            <a href="/" className="navbar-text navbar-link">Zahtjevi</a>
            <a href="/" className="navbar-text navbar-link">Oglasi</a>
              {logoutButton}
              {loginButton}
              {registerButton}
          </div>
      </div>
    );
}

export default Navbar;