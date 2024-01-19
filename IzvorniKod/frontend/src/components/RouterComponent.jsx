// eslint-disable-next-line no-unused-vars
import React from "react";
import {Route, Routes} from "react-router-dom";
import {LoginPage} from "../pages/LoginPage.jsx";
import {LandingPage} from "../pages/LandingPage.jsx";
import {RegisterPage} from "../pages/RegisterPage.jsx";
import {FeaturesPage} from "../pages/FeaturesPage.jsx";
import {ContactPage} from "../pages/ContactPage.jsx";
// eslint-disable-next-line no-unused-vars
export function RouterComponent(props) {
    return (
        <Routes>
        <Route path="/" element={<LandingPage/>}/>
        <Route path="/login" element={<LoginPage/>}/>
        <Route path="/register" element={<RegisterPage/>}/>
        <Route path="/features" element={<FeaturesPage/>}/>
        <Route path="/contact" element={<ContactPage/>}/>
    </Routes>
    );
}