// eslint-disable-next-line no-unused-vars
import React, {useEffect} from 'react';
import {useNavigate, useLocation} from 'react-router-dom';
import {useDispatch, useSelector} from 'react-redux';
import {reset} from '../features/auth/authSlice';

export function LoginChecker() {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const location = useLocation()

    const { user, isLoading, isError, isSuccess, message } = useSelector(state => state.auth);

    useEffect(() => {
        return (() => {
            dispatch(reset());
        })
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    useEffect(() => {
        if (user) {
          const allowedPaths = ['/home', '/map', '/wallet', '/settings', '/profile', '/stats'];
          const currentPath = location.pathname;
    
          if (allowedPaths.includes(currentPath)) {
            return;
          }
    
          const redirectPaths = ['/login', '/register'];
    
          if (redirectPaths.includes(currentPath)) {
            navigate('/home');
          }
        } else if (!user) {
          navigate('/');
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
      }, [user, isLoading, isError, isSuccess, message, navigate, location.pathname]);
}