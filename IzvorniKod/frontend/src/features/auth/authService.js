// eslint-disable-next-line no-unused-vars
import { isAsyncThunkAction } from '@reduxjs/toolkit';
import axios from 'axios'


const register = async (data) => {
    const response = await axios.post('http://localhost:8080/api/registration', data);
    
    console.log(response);
    return response.data;
}


const login = async (jsonData) => {
    console.log(jsonData);


    const response = await fetch('http://localhost:8080/api/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData,
        });
    
    if (response.ok) {
        const user =  await response.text();
        localStorage.setItem('user', user)
    }
    
    return response
}

const logout = async () => {
    localStorage.removeItem('user');
    console.log("mice");
    const response = await axios.post('/api/logout');
    return response.data
}

export function logout2(){
    localStorage.removeItem('user');
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
