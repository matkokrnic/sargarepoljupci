import React, {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {useDispatch, useSelector} from 'react-redux';
import {reset} from '../features/auth/authSlice';

export function LoginChecker() {
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const { user, isLoading, isError, isSuccess, message } = useSelector(state => state.auth);

    useEffect(() => {
        if (user) {
            navigate('/home');
        } else{
            navigate('/');
        }
    } , [user, isLoading, isError, isSuccess, message])
}