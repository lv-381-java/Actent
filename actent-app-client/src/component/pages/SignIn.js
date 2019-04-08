import React from 'react';
import { Link } from 'react-router-dom';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import axios from 'axios';
import { NotificationContainer, NotificationManager } from 'react-notifications';
import 'react-notifications/lib/notifications.css';
import { saveAuthorizationToken, setAuthorizationHeader, getTokenFromCredentials } from '../../util/apiUtils';

export default class SignIn extends React.Component {
    state = {
        usernameOrEmail: undefined,
        password: undefined,
    };

    handleEmail = event => {
        this.setState({ usernameOrEmail: event.target.value });
    };

    handlePassword = event => {
        this.setState({ password: event.target.value });
    };

    sendData = event => {
        const user = {
            password: this.state.password,
            usernameOrEmail: this.state.usernameOrEmail,
        };

        getTokenFromCredentials(user)
            .then(response => {
                console.log(response.data);
                response.data.accessToken
                    ? this.handleLogin(response.data.accessToken)
                    : Promise.reject('Access token is undefined.');
                this.props.history.push('/');
            })
            .catch(error => {
                NotificationManager.error('Invalid E-mail or Password!', 'Error!', 5000);
            });
    };

    handleLogin = accessToken => {
        saveAuthorizationToken(accessToken);
        setAuthorizationHeader();
    };

    isValid = () => {
        if (this.state.usernameOrEmail && this.state.usernameOrEmail.length) {
            return true;
        } else {
            return false;
        }
    };

    render() {
        return (
            <div className='FormCenter'>
                <form className='FormFields'>
                    <div className='FormField'>
                        <TextField
                            id='outlined-email-input'
                            label='Email or Login'
                            className='FormField__Input'
                            type='login'
                            name='email'
                            autoComplete='email'
                            margin='normal'
                            variant='outlined'
                            onChange={this.handleEmail}
                        />
                    </div>
                    <div className='FormField'>
                        <TextField
                            id='outlined-password-input'
                            label='Password'
                            className='FormField__Input'
                            type='password'
                            autoComplete='current-password'
                            margin='normal'
                            variant='outlined'
                            onChange={this.handlePassword}
                        />
                    </div>

                    <div className='FromField'>
                        <Button
                            className='FormField__Button'
                            variant='contained'
                            color='primary'
                            disabled={!this.isValid()}
                            onClick={this.sendData}
                        >
                            Sign in
                        </Button>
                        <Link to='/auth/signUp' className='FormField__Link'>
                            Create an account
                        </Link>
                    </div>
                </form>
                <NotificationContainer />
            </div>
        );
    }
}
