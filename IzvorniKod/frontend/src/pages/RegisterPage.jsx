// RegistrationPage.jsx
import React from 'react';
import { RegisterForm } from '../components/RegisterForm';
import { Fragment } from 'react'
import { Disclosure, Menu, Transition } from '@headlessui/react'
import { Bars3Icon, BellIcon, XMarkIcon } from '@heroicons/react/24/outline'

export  function RegisterPage() {
  return (
    <>
      <div className="h-full bg-gray-100">
      <div className="h-full">
      <div className="min-h-full">
        <Disclosure as="nav" className="bg-gray-800">
          {({ open }) => (
            <>
              <div className="px-4 mx-auto max-w-7xl sm:px-6 lg:px-8">
                <div className="flex justify-center h-16 items-left">
                  <div className="flex items-center">
                    <div className="flex-shrink-0">
                        <a href="/">
                      <img
                        className="w-auto h-12 mx-1"
                        src="../images/slova_pokraj.svg"
                        alt="SpotPicker"
                      />
                      </a>
                    </div>
                    </div>
                  </div>
                  
                </div>
            </>
          )}
        </Disclosure>

        <header className="bg-white shadow">
          <div className="px-4 py-6 mx-auto max-w-7xl sm:px-6 lg:px-8">
            <h1 className="flex justify-center text-3xl font-bold tracking-tight text-gray-900">Registracija</h1>
          </div>
        </header>
        <main>
          <div className="flex items-center justify-center h-full py-6 mx-auto max-w-7xl sm:px-6 lg:px-8">{<RegisterForm/>}</div>
        </main>
      </div>
      </div>
      </div>
    </>
  )
}

