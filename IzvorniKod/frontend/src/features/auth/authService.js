import { isAsyncThunkAction } from '@reduxjs/toolkit';
import axios from 'axios'


const register = async (data) => {
    const response = await axios.post('/api/listunreguser', data);
    
    console.log(response);
    return response.data;
}


const login = async (data) => {

    /*const params = new URLSearchParams();
    params.append('username', data.username);
    params.append('pass', data.pass);*/

    //console.log(params.toString());

    const response = await fetch('/api/login', data) //axios.post('/login', params);

    const headerUser = new Map(response.headers).get('user');
    
    
    console.log(headerUser)
    if (headerUser) {
        localStorage.setItem('user', JSON.stringify(headerUser))
    }
    
    return headerUser
}

const logout = async () => {
    const response = await axios.post('/api/logout');
    localStorage.removeItem('user')
    return response.data
}


const getUserInfo =  async () => {
    const response = await axios.get('/api/reguser/user');
    return response.data;
}

export const authService = {
    register,
    logout,
    login,
    getUserInfo,
}
