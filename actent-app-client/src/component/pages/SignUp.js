import React from 'react';
import { Link } from 'react-router-dom';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import { NotificationContainer, NotificationManager } from 'react-notifications';
import 'react-notifications/lib/notifications.css';
import { registerUser } from '../../util/apiUtils';

export default class SignUp extends React.Component {
    state = {
        name: undefined,
        surname: undefined,
        username: undefined,
        password: undefined,
        repeatpassword: undefined,
        email: undefined,
        errors: [],
    };

    handleName = event => {
        this.setState({ name: event.target.value });
    };
    handleSurname = event => {
        this.setState({ surname: event.target.value });
    };
    handleUsername = event => {
        this.setState({ username: event.target.value });
    };
    handlePassword = event => {
        this.setState({ password: event.target.value });
    };
    handlePasswordRepeat = event => {
        this.setState({ repeatpassword: event.target.value });
    };
    handleEmail = event => {
        this.setState({ email: event.target.value });
    };

    isValid = () => {
        if (
            this.state.name &&
            this.state.surname &&
            this.state.username &&
            this.state.password &&
            this.state.repeatpassword &&
            this.state.email
        ) {
            return true;
        } else {
            return false;
        }
    };
    responseGoogleSuccess = response => {
        let userName = response.w3.ofa;
        let userSurname = response.w3.wea;
        let userEmail = response.w3.U3;
        this.setState({
            surname: userSurname,
            username: userName,
            email: userEmail,
        });
    };

    sendData = event => {
        event.preventDefault();
        let suberrors = [];

        const user = {
            email: this.state.email,
            firstName: this.state.name,
            lastName: this.state.surname,
            login: this.state.username,
            password: this.state.password,
        };

        if (this.state.password !== this.state.repeatpassword) {
            let error = 'Passwords must match';
            console.log(error);
            suberrors.push(error);
        } else {
            if (this.state.password.length < 6) {
                let error = 'Password must be at least six symbols long.';
                console.log(error);
                suberrors.push(error);
            }
        }
        if (this.state.username.length < 5) {
            let error = 'Login must be at least five symbols long';
            console.log(error);
            suberrors.push(error);
        }
        if (suberrors.length > 0) {
            console.log(suberrors);
            suberrors.forEach(error => {
                NotificationManager.error(error, 'Error', 5000);
            });
            return;
        }

        registerUser(user)
            .then(res => {
                NotificationManager.success('Verification message has been sent to your e-mail', 'Check your e-mail');
            })
            .catch(error => {
                this.setState({ errors: error.response.data.error.debugMessage });
                NotificationManager.error(this.state.errors, 'Error', 5000);
            });
    };

    render() {
        const responseGoogle = response => {
            console.log(response);
        };
        return (
            <div className='FormCenter'>
                <form className='FormFields'>
                    <div className='FormField'>
                        <TextField
                            id='outlined-email-input'
                            label='Name'
                            className='FormField__Input'
                            type='text'
                            name='name'
                            autoComplete='name'
                            margin='normal'
                            variant='outlined'
                            onChange={this.handleName}
                        />
                    </div>
                    <div className='FormField'>
                        <TextField
                            id='outlined-email-input'
                            label='Surname'
                            className='FormField__Input'
                            type='text'
                            value={this.state.surname}
                            name='surname'
                            autoComplete='surname'
                            margin='normal'
                            variant='outlined'
                            onChange={this.handleSurname}
                        />
                    </div>
                    <div className='FormField'>
                        <TextField
                            id='outlined-email-input'
                            label='Username'
                            className='FormField__Input'
                            type='text'
                            name='username'
                            value={this.state.username}
                            autoComplete='username'
                            margin='normal'
                            variant='outlined'
                            helperText='Login must be at least 5 characters long.'
                            onChange={this.handleUsername}
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
                            helperText='Passwords must be at least 6 characters long.(A-Z, a-z, 0-9)'
                            onChange={this.handlePassword}
                        />
                    </div>
                    <div className='FormField'>
                        <TextField
                            id='outlined-password-input'
                            label='Repeat password'
                            className='FormField__Input'
                            type='password'
                            autoComplete='current-password'
                            margin='normal'
                            variant='outlined'
                            helperText='Passwords must be at least 6 characters long.(A-Z, a-z, 0-9)'
                            onChange={this.handlePasswordRepeat}
                        />
                    </div>
                    <div className='FormField'>
                        <TextField
                            id='outlined-email-input'
                            label='Email'
                            className='FormField__Input'
                            type='email'
                            name='email'
                            value={this.state.email}
                            autoComplete='email'
                            margin='normal'
                            variant='outlined'
                            onChange={this.handleEmail}
                        />
                    </div>
                    <div className='FromField'>
                        <Button
                            className='FormField__Button'
                            variant='contained'
                            color='primary'
                            disabled={!this.isValid()}
                            onClick={this.sendData}>
                            Sign up
                        </Button>
                        <Link to='/auth/signIn' className='FormField__Link'>
                            I'm already member
                        </Link>
                    </div>
                </form>
                <NotificationContainer />
            </div>
        );
    }
}
