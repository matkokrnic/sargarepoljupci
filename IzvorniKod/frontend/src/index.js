import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";

// Import komponenata za različite rute
import Home from "./views/home.js";
import Register from "./views/register.js";
import Login from "./views/login.js";

// Globalni CSS stilovi
import './index.css';

// Glavna komponenta koja sadrži rute
export default function App() {
  return (
    <BrowserRouter>
      {/* Definicija ruta */}
      <Routes>
        {/* Ruta za početnu stranicu */}
        <Route path="/" element={<Home />} />
        {/* Ruta za registraciju */}
        <Route path="/register" element={<Register />} />
        {/* Ruta za prijavu */}
        <Route path="/login" element={<Login />} />
      </Routes>
    </BrowserRouter>
  );
}

// Kreiranje korijenskog elementa i renderiranje glavne komponente
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(<App />);
