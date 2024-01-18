/* eslint-disable no-unused-vars */
import React, {useEffect, useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import {useDispatch, useSelector} from 'react-redux';
import {login, reset} from '../features/auth/authSlice';

export function LoginPage() {
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const { user, isLoading, isError, isSuccess, message } = useSelector(state => state.auth);

    useEffect(() => {
        return (() => {
            dispatch(reset());
        })
    }, []);

    useEffect(() => {
        if (user) {
            navigate('/home');
        } else{
            navigate('/login');
        }
    } , [user, isLoading, isError, isSuccess, message])

    const [email, setEmail] = useState('');

    const handleEmailChange = (event) => {
        setEmail(event.target.value);
    };

    const [password, setPassword] = useState('');

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };


    function onSubmit(event) { 
        event.preventDefault();

        dispatch(reset());


        const body = `email=${email}&password=${password}`;

        const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: body
        };
        /*console.log(options);*/
        dispatch(login(options));
        /*
        fetch('/login', options)
        .then(response => {
            if (response.status === 401) {
            } else {
            props.onLogin();
            }
        });*/

    }
    return (
      <>
      <div className="h-full bg-white">
        <div className="h-full">         
                <div className="flex flex-col justify-center flex-1 min-h-full px-6 py-12 lg:px-8">
                <div className="sm:mx-auto sm:w-full sm:max-w-sm">
                    <img
                    className="w-auto h-10 mx-auto"
                    src="https://tailwindui.com/img/logos/mark.svg?color=indigo&shade=600"
                    alt="SpotPicker"
                    />
                    <h2 className="mt-10 text-2xl font-bold leading-9 tracking-tight text-center text-gray-900">
                    Prijavite se u vaš račun
                    </h2>
                </div>
        
                <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
                    <form className="space-y-6" action="#" method="POST">
                    <div>
                        <label htmlFor="email" className="block text-sm font-medium leading-6 text-gray-900">
                        Adresa e-pošte
                        </label>
                        <div className="mt-2">
                        <input
                            id="email"
                            name="email"
                            type="email"
                            autoComplete="email"
                            required
                            value={email}
                            onChange={handleEmailChange}
                            className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                        />
                        </div>
                    </div>
        
                    <div>
                        <div className="flex items-center justify-between">
                        <label htmlFor="password" className="block text-sm font-medium leading-6 text-gray-900">
                            Lozinka
                        </label>
                        <div className="text-sm">
                            <a href="#" className="font-semibold text-indigo-600 hover:text-indigo-500">
                            Zaboravili ste lozinku?
                            </a>
                        </div>
                        </div>
                        <div className="mt-2">
                        <input
                            id="password"
                            name="password"
                            type="password"
                            autoComplete="current-password"
                            required
                            value={password}
                            onChange={handlePasswordChange}
                            className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                        />
                        </div>
                    </div>
        
                    <div>
                        <button
                        type="submit"
                        onClick={onSubmit}
                        className="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                        >
                        Prijavi se
                        </button>
                    </div>
                    </form>
        
                    <p className="mt-10 text-sm text-center text-gray-500">
                        Nemate račun?{' '}
                    <a href="/register" className="font-semibold leading-6 text-indigo-600 hover:text-indigo-500">
                        Registrirajte se
                    </a>
                    </p>
                </div>
                </div>
            </div>
        </div>
      </>
    )
  }
  