import React, {useEffect, useState} from 'react';
import { PhotoIcon, UserCircleIcon, CheckIcon, XCircleIcon, CheckCircleIcon } from '@heroicons/react/24/solid'
import {Link, useNavigate} from 'react-router-dom';
import {useDispatch, useSelector} from 'react-redux';
import {register, reset} from '../features/auth/authSlice';

export function RegisterForm() {

    const navigate = useNavigate();
    const dispatch = useDispatch();

    const { user, isLoading, isError, isSuccess, message } = useSelector(state => state.auth);

    useEffect(() => {
        return () => {
            dispatch(reset());
        }
    }, []);

    useEffect(() => {
        if (isSuccess || user) {
            dispatch(reset());
            navigate('/login');
        }
    } , [user, isLoading, isError, isSuccess, message]);

    const [form, setForm] = React.useState({
        firstName: '',
        lastName: '',
        username: '',
        email: '',
        iban: '',
        IDphoto: '',
        role: '0',
        password: '',
      });

    function onChange(event) {
        const { name, value } = event.target;
        setForm((oldForm) => ({ ...oldForm, [name]: value }));
    }

    const [password, setPassword] = useState('');
    const [passwordAgain, setPasswordAgain] = useState('');
    const [samePassword, setSamePassword] = useState(false);
    const [photo, setPhoto] = useState(null);

    function checkSamePassword() {
        console.log(form);
        if(password.length < 6 || password.length < 6)
            return;
        if(password == passwordAgain)
            setSamePassword(true);
        else if(password !== passwordAgain)
            setSamePassword(false);
    }

    useEffect(() => {
        checkSamePassword();
    }, [password, passwordAgain]);

    const handlePhotoChange = (event) => {
        setPhoto(event.target.files[0]);
    }

    function onSubmit(event) {
        event.preventDefault();
        var bodyFormData = new FormData();
            bodyFormData.append("korisnickoIme", form.username);
            bodyFormData.append("lozinka", form.password);
            bodyFormData.append("email", form.email);
            bodyFormData.append("iban", form.iban);
            bodyFormData.append("ime", form.firstName);
            bodyFormData.append("prezime", form.lastName);
            bodyFormData.append("slikaOsobne", "");
            bodyFormData.append("Uloga", form.role);
        console.log(bodyFormData.values);
        dispatch(register(bodyFormData));

        
    }

  return (
    <form onSubmit={onSubmit}>
      <div className="space-y-12">
        <div className="pb-12 border-b border-gray-900/10">
          <h2 className="text-base font-semibold leading-7 text-gray-900">Profil</h2>
          <p className="mt-1 text-sm leading-6 text-gray-600">
            Ove informacije će biti prikazane javno.
          </p>

          <div className="grid grid-cols-1 mt-10 gap-x-6 gap-y-8 sm:grid-cols-6">
            <div className="sm:col-span-4">
              <label htmlFor="username" className="block text-sm font-medium leading-6 text-gray-900">
                Korisničko ime
              </label>
              <div className="mt-2">
                <div className="flex rounded-md shadow-sm ring-1 ring-inset ring-gray-300 focus-within:ring-2 focus-within:ring-inset focus-within:ring-indigo-600 sm:max-w-md">
                  <input
                    type="text"
                    name="username"
                    id="username"
                    autoComplete="username"
                    onChange={onChange}
                    className="block flex-1 border-0 bg-transparent py-1.5 pl-1 text-gray-900 placeholder:text-gray-400 focus:ring-0 sm:text-sm sm:leading-6"
                    placeholder="ivanhorvat"
                    required
                  />
                </div>
              </div>
            </div>

            <div className="col-span-full">
              <label htmlFor="photo" className="block text-sm font-medium leading-6 text-gray-900">
                Slika profila
              </label>
              <div className="flex items-center mt-2 gap-x-3">
                <UserCircleIcon className="w-12 h-12 text-gray-300" aria-hidden="true" />
                <button
                  type="button"
                  className="rounded-md bg-white px-2.5 py-1.5 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50"
                >
                  Promijeni
                </button>
              </div>
            </div>
          </div>
        </div>

        <div className="pb-12 border-b border-gray-900/10">
          <h2 className="text-base font-semibold leading-7 text-gray-900">Osobne informacije</h2>
          <p className="mt-1 text-sm leading-6 text-gray-600">Koristite trajnu adresu gdje možete primati poštu.</p>

          <div className="grid grid-cols-1 mt-10 gap-x-6 gap-y-8 sm:grid-cols-6">
            <div className="sm:col-span-3">
              <label htmlFor="firstName" className="block text-sm font-medium leading-6 text-gray-900">
                Ime
              </label>
              <div className="mt-2">
                <input
                  required
                  type="text"
                  name="firstName"
                  id="firstName"
                  autoComplete="name"
                  onChange={onChange}
                  className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                />
              </div>
            </div>

            <div className="sm:col-span-3">
              <label htmlFor="lastName" className="block text-sm font-medium leading-6 text-gray-900">
                Prezime
              </label>
              <div className="mt-2">
                <input
                  required
                  type="text"
                  name="lastName"
                  id="lastName"
                  autoComplete="surname"
                  onChange={onChange}
                  className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                />
              </div>
            </div>

            <div className="sm:col-span-4">
              <label htmlFor="password" className="block text-sm font-medium leading-6 text-gray-900">
                Lozinka
              </label>
              <div className="mt-2">
                <input
                  required
                  id="password"
                  name="password"
                  type="password"
                  autoComplete="password"
                  onChange={(e) => {
                    setPassword(e.target.value);
                    onChange(e);
                  }}
                  minLength="6" // Minimum length of 6 characters
                  pattern=".{6,}" // Minimum length of 6 characters using a regex pattern
                  title="Lozinka mora sadržavati barem 6 znakova"
                  className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                />
              </div>
            </div>

            <div className="sm:col-span-4">
              <label htmlFor="password" className="block text-sm font-medium leading-6 text-gray-900">
                Ponovite lozinku
              </label>
              <div className="flex items-center mt-2 gap-x-3">
                <input
                  required
                  id="repeatpassword"
                  name="repeatpassword"
                  type="password"
                  autoComplete="password"
                  onChange={(e) => {
                    setPasswordAgain(e.target.value);
                    onChange(e);
                    checkSamePassword();
                  }}
                  minLength="6" // Minimum length of 6 characters
                  pattern=".{6,}" // Minimum length of 6 characters using a regex pattern
                  title="Lozinka mora sadržavati barem 6 znakova"
                  className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                />
                {samePassword ? (
                <CheckCircleIcon className="w-8 h-8 text-green-600" aria-hidden="true" />
                ) : null}
              </div>
            </div>

            <div className="sm:col-span-4">
              <label htmlFor="email" className="block text-sm font-medium leading-6 text-gray-900">
                Adresa e-pošte
              </label>
              <div className="mt-2">
                <input
                  required
                  id="email"
                  name="email"
                  type="email"
                  autoComplete="email"
                  onChange={onChange}
                  className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                />
              </div>
            </div>

            <div className="sm:col-span-3">
              <label htmlFor="role" className="block text-sm font-medium leading-6 text-gray-900">
                Uloga
              </label>
              <div className="mt-2">
                <select
                  required
                  id="role"
                  name="role"
                  autoComplete="role-name"
                  onChange={onChange}
                  className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:max-w-xs sm:text-sm sm:leading-6"
                >
                  <option value={"0"}>Klijent</option>
                  <option value={"1"}>Voditelj parkinga</option>
                </select>
              </div>
            </div>

            <div className="col-span-full">
              <label htmlFor="iban" className="block text-sm font-medium leading-6 text-gray-900">
                IBAN račun
              </label>
              <div className="mt-2">
                <input
                  required
                  type="text"
                  name="iban"
                  id="iban"
                  autoComplete="off"
                  onChange={onChange}
                  pattern="^[HR]{2}\d{21}$"
                  title="Molimo unesite valjani hrvatski IBAN račun"
                  className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                />
              </div>
            </div>

            <div className="col-span-full">
              <label htmlFor="ID-photo" className="block text-sm font-medium leading-6 text-gray-900">
                Slika osobne iskaznice
              </label>
              <div className="flex justify-center px-6 py-10 mt-2 border border-dashed rounded-lg border-gray-900/25">
                <div className="text-center">
                  <PhotoIcon className="w-12 h-12 mx-auto text-gray-300" aria-hidden="true" />
                  <div className="flex mt-4 text-sm leading-6 text-gray-600">
                    <label
                      htmlFor="file-upload"
                      className="relative font-semibold text-indigo-600 bg-white rounded-md cursor-pointer focus-within:outline-none focus-within:ring-2 focus-within:ring-indigo-600 focus-within:ring-offset-2 hover:text-indigo-500"
                    >
                      <span>Prenesi datoteku</span>
                      <input id="file-upload" required name="file-upload" type="file" className="sr-only" />
                    </label>
                    <p className="pl-1">ili je povucite i ispustite.</p>
                  </div>
                  <p className="text-xs leading-5 text-gray-600">PNG, JPG, GIF up to 10MB</p>
                </div>
              </div>
            </div>
            
          </div>
        </div>

        <div className="pb-12 border-b border-gray-900/10">
          <h2 className="text-base font-semibold leading-7 text-gray-900">Pravila privatnosti</h2>
          <p className="mt-1 text-sm leading-6 text-gray-600">
            Uvijek ćemo vas obavijestiti o važnim promjenama, sada trebate prihvaćate naše uvjete.
          </p>

          <div className="mt-10 space-y-10">
            <fieldset>
              <div className="mt-6 space-y-6">
                <div className="relative flex gap-x-3">
                  <div className="flex items-center h-6">
                    <input
                      required
                      id="comments"
                      name="comments"
                      type="checkbox"
                      className="w-4 h-4 text-indigo-600 border-gray-300 rounded focus:ring-indigo-600"
                    />
                  </div>
                  <div className="text-sm leading-6">
                    <p className="text-gray-500">Prihvaćate naša pravila o zaštiti privatnosti.</p>
                  </div>
                </div>
                <div className="relative flex gap-x-3">
                  <div className="flex items-center h-6">
                    <input
                      required
                      id="candidates"
                      name="candidates"
                      type="checkbox"
                      className="w-4 h-4 text-indigo-600 border-gray-300 rounded focus:ring-indigo-600"
                    />
                  </div>
                  <div className="text-sm leading-6">
                    <p className="text-gray-500"> Prihvaćate naša pravila o upotrebi kolačića.</p>
                  </div>
                </div>
              </div>
            </fieldset>
          </div>
        </div>
      </div>

      <div className="flex items-center justify-end mt-6 gap-x-6">
        <button type="button" className="text-sm font-semibold leading-6 text-gray-900">
          <a href="/">Odustajem</a>
        </button>
        <button
          type="submit"
          className="px-3 py-2 text-sm font-semibold text-white bg-indigo-600 rounded-md shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
        >
          Registriraj se
        </button>
      </div>
    </form>
  )
}
