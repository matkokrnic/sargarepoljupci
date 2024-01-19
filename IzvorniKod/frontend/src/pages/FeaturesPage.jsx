// eslint-disable-next-line no-unused-vars
import React from 'react'
import {DarkFooter} from "../components/DarkFooter.jsx";
import { FeaturesContent } from '../components/FeaturesContent.jsx';
import { HeaderComponent } from '../components/HeaderComponent.jsx';

export function FeaturesPage() {
  return (
    <div className="bg-white">
      <HeaderComponent />
      <FeaturesContent />
      <DarkFooter />
    </div>
  )
}