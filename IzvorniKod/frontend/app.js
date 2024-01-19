/* eslint-disable no-undef */
// Uvoz potrebnih modula
const express = require("express");
const { createProxyMiddleware } = require("http-proxy-middleware");
require("dotenv").config(); // Uvoz modula za rad s .env datotekom
const path = require("path");

// Inicijalizacija Express aplikacije
const app = express();

// Učitavanje konfiguracijskih varijabli iz .env datoteke
const { PORT } = process.env;
const { HOST } = process.env;
const { API_BASE_URL } = process.env;

// Postavljanje proxy middleware-a za ruta koje započinju s "/api"
app.use(
  "/api",
  createProxyMiddleware({
    target: API_BASE_URL, // Postavljanje ciljnog URL-a na koji će se preusmjeravati zahtjevi
    changeOrigin: true, // Postavljanje promjene porijekla na true omogućuje prepoznavanje promjena porijekla zahtjeva
  })
);

// Postavljanje Express middleware-a za posluživanje statičkih datoteka iz build direktorija
app.use(express.static(path.join(__dirname, 'build')));

// Postavljanje servera da osluškuje na određenom portu i hostu
app.listen(PORT, HOST, () => {
  console.log(`Starting Proxy at ${HOST}:${PORT}`);
});

// Express ruta koja poslužuje index.html za sve druge putanje ("*")
app.get("*", async (req, res) => {
  res.sendFile(path.join(__dirname, 'build', 'index.html'));
});
