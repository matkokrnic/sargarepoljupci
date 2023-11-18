import React, { useState } from 'react';
import PasswordChecklist from 'react-password-checklist';
import { Helmet } from 'react-helmet';

// Import komponenata
import Navbar from './partials/navbar';
import Footer from './partials/footer';
import './home.css';
import '../index.css';

// Import materijal UI komponenata
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import NativeSelect from '@mui/material/NativeSelect';
import { alpha, styled } from '@mui/material/styles';
import axios, { AxiosError } from 'axios';

// Stilizirani TextField
const StyledTextField = styled(TextField)({
  '& label.Mui-focused': {
    color: 'black',
  },
  '& .MuiInput-underline:after': {
    borderBottomColor: 'black',
  },
  '& .MuiOutlinedInput-root': {
    '& fieldset': {
      borderColor: 'black',
    },
    '&:hover fieldset': {
      borderColor: 'black',
    },
    '&.Mui-focused fieldset': {
      borderColor: 'black',
    },
  },
});

function Register() {
  // State hook-ovi
  const [password, setPassword] = useState('');
  const [isDisabled, setIsDisabled] = useState(false);
  const [passwordAgain, setPasswordAgain] = useState('');
  const [form, setForm] = React.useState({
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    role: '',
    password: '',
    confirmPassword: '',
  });

  // Funkcija za promjenu polja u formi
  function onChange(event) {
    const { name, value } = event.target;
    setForm((oldForm) => ({ ...oldForm, [name]: value }));
  }

  // Provjera ispravnosti forme
  function isValid() {
    const { firstName, lastName, username, email, role, password, confirmPassword } = form;
    return (
      firstName.length > 0 &&
      lastName.length > 0 &&
      username.length > 0 &&
      password.length >= 8 &&
      password == confirmPassword &&
      /\S+@\S+\.\S+/.test(email) &&
      role != 0
    );
  }

  // Funkcija za obradu forme pri submitu
  function onSubmit(e) {
    e.preventDefault();
    setIsDisabled(true);
    axios
      .post('/api/registration', {
        username: form.username,
        firstName: form.firstName,
        lastName: form.lastName,
        password: form.password,
        email: form.email,
        roleId: form.role,
      })
      .then(async (response) => {
        console.log(response);
        window.location.href = '/';
        setIsDisabled(false);
      })
      .catch((err) => {
        console.log(err);
        alert(err.response.data.message);
      });
  }

  return (
    <div className="page-container">
      <Helmet>
        <title>SpotPicker | Registracija</title>
      </Helmet>

      <Navbar />

      <div className="form-section-container ">
        <div className="form-container background-honey">
          <Box
            sx={{
              padding: 5,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
            }}
          >
            <Typography component="h1" variant="h5" className="text-storm">
              Pridruži se!
            </Typography>
            <Box component="form" sx={{ mt: 3 }}>
              <Grid container spacing={2}>
                <Grid item xs={12}>
                  <StyledTextField
                    name="firstName"
                    required
                    fullWidth
                    id="firstName"
                    label="Ime"
                    onChange={onChange}
                    value={form.firstName}
                  />
                </Grid>
                <Grid item xs={12}>
                  <StyledTextField
                    required
                    fullWidth
                    id="lastName"
                    label="Prezime"
                    name="lastName"
                    onChange={onChange}
                    value={form.lastName}
                  />
                </Grid>
                <Grid item xs={12}>
                  <StyledTextField
                    name="username"
                    required
                    fullWidth
                    id="username"
                    label="Korisničko ime"
                    onChange={onChange}
                    value={form.username}
                  />
                </Grid>
                <Grid item xs={12}>
                  <StyledTextField
                    required
                    fullWidth
                    id="email"
                    label="Email Adresa"
                    name="email"
                    onChange={onChange}
                    value={form.email}
                  />
                </Grid>

                <Grid item xs={12}>
                  <NativeSelect
                    inputProps={{
                      name: 'role',
                      id: 'role',
                    }}
                    fullWidth
                    required
                    defaultValue={0}
                    onChange={onChange}
                    value={form.role}
                  >
                    <option value={0}>Odabir uloge</option>
                    <option value={1}>Klijent</option>
                    <option value={2}>Voditelj parkinga</option>
                  </NativeSelect>
                </Grid>

                <Grid item xs={12}>
                  <StyledTextField
                    required
                    fullWidth
                    onChange={(e) => {
                      setPassword(e.target.value);
                      onChange(e);
                    }}
                    name="password"
                    label="Lozinka"
                    type="password"
                    id="password"
                    value={form.password}
                  />
                </Grid>
                <Grid item xs={12}>
                  <StyledTextField
                    required
                    fullWidth
                    onChange={(e) => {
                      setPasswordAgain(e.target.value);
                      onChange(e);
                    }}
                    name="confirmPassword"
                    label="Potvrdi Lozinku"
                    type="password"
                    id="confirmPassword"
                    value={form.confirmPassword}
                  />
                </Grid>

                <Grid item xs={12}>
                  <PasswordChecklist
                    rules={['minLength', 'match']}
                    minLength={8}
                    value={password}
                    valueAgain={passwordAgain}
                    onChange={(isValid) => {}}
                    messages={{
                      minLength: 'Lozinka mora imati barem 8 znakova.',
                      match: 'Lozinke se podudaraju.',
                    }}
                  />
                </Grid>

                <Grid item xs={12}>
                  <div className="form-button-container">
                    <button
                      type="submit"
                      className="button button-primary"
                      variant="contained"
                      disabled={!isValid() || isDisabled}
                      onClick={onSubmit}
                    >
                      Registracija
                    </button>
                  </div>
                </Grid>
              </Grid>
            </Box>
          </Box>
        </div>
      </div>
    </div>
  );
}

export default Register;
