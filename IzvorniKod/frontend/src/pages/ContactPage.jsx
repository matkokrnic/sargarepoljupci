// eslint-disable-next-line no-unused-vars
import React from 'react'
import {DarkFooter} from "../components/DarkFooter.jsx";
import { ContactContent } from '../components/ContactContent.jsx';
import { HeaderComponent } from '../components/HeaderComponent.jsx';

export function ContactPage() {
  return (
    <div className="bg-white">
      <HeaderComponent />
      <ContactContent />
      <DarkFooter />
    </div>
  )
}