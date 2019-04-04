import React from 'react';
import axios from 'axios';
import { apiUrl } from './Profile.js';
import { getImageUrl } from './ProfileView';
import FileUpload from './FileUpload';
import styles from './style.css';
import { Button, Card, Typography, TextField } from '@material-ui/core';
// import { DatePicker, MuiPickersUtilsProvider } from 'material-ui-pickers';
import DatePicker from 'material-ui/DatePicker';
// import DateFnsUtils from '@date-io/date-fns';

export default class ProfileEdit extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            userId: this.props.profileData.userId,
            firstName: this.props.profileData.firstName,
            lastName: this.props.profileData.lastName,
            email: this.props.profileData.email,
            phone: this.props.profileData.phone,
            login: this.props.profileData.login,
            address: this.props.profileData.address,
            birthday: new Date(this.props.profileData.birthday),
            bio: this.props.profileData.bio,
            interests: this.props.profileData.interests,
            imageName: this.props.profileData.avatar !== null ? `${this.props.profileData.avatar.filePath}` : '',
            imageData: {},
            imageId: undefined,
            filePath: undefined,
            errorFirstName: '',
            errorLastName: '',
            errorEmail: '',
            errorPhone: '',
            errorLogin: '',
            errorAddress: '',
            errorBirthDate: '',
            errorBio: '',
        };
        console.log(props);
    }

    handleFirstName = event => {
        if (event.target.length > 2 || event.target.length < 30) {
            this.setState({ firstName: event.target.value, errorFirstName: '' });
        } else {
            this.setState({ errorFirstName: 'First name must be from 2 to 30 symbols' });
        }
    };

    handleLastName = event => {
        if (event.target.length > 2 || event.target.length < 30) {
            this.setState({ lastName: event.target.value, errorLastName: '' });
        } else {
            this.setState({ errorLastName: 'First name must be from 2 to 30 symbols' });
        }
    };

    handlePhone = event => {
        const regex = /^$|[0-9]{10,12}/;
        if (regex.test(event.target.value === true)) {
            this.setState({ phone: event.target.value, errorPhone: '' });
        } else {
            this.setState({ errorPhone: 'Phone number not valid' });
        }
    };

    handleLogin = event => {
        if (event.target.length > 2 || event.target.length < 30) {
            this.setState({ login: event.target.value, errorLogin: '' });
        } else {
            this.setState({ errorLogin: 'First name must be from 2 to 30 symbols' });
        }
    };

    handleAddress = event => {
        this.setState({ address: event.target.value });
    };

    handleBirthday = (event, date) => {
        this.setState({ birthday: date });
        console.log(date);
        console.log(event);
    };

    handleBio = event => {
        if (event.target.value.length < 500) {
            this.setState({ bio: event.target.value, errorBio: '' });
        } else {
            this.setState({ errorBio: 'Bio must be up to 500 symbols' });
        }
    };

    handleInterests = event => {
        this.setState({ interests: event.target.value });
    };

    handleEmail = event => {
        const regex = /^\S+@\S+\.\S+$/;
        if (regex.test(event.target.value) === true) {
            this.setState({ errorEmail: '', email: event.target.value });
        } else {
            this.setState({ errorEmail: 'Error Email' });
        }
    };

    getBirthday = () => {
        if (this.state.birthday !== null) {
            return (
                this.state.birthday.getFullYear() +
                '-' +
                this.handleDigitsInMonth(this.state.birthday.getMonth() + 1) +
                '-' +
                this.handleDigitsInDate(this.state.birthday.getDate())
            );
        } else {
            return null;
        }
    };

    handleDigitsInDate = i => {
        return i < 10 ? `0${i}` : i;
    };

    handleDigitsInMonth = i => {
        return i < 10 ? `0${i + 1}` : i;
    };

    saveUserSettings = () => {
        const url = apiUrl + /users/ + this.state.userId;

        const data = {
            firstName: this.state.firstName,
            lastName: this.state.lastName,
            login: this.state.login,
            address: this.state.address,
            birthDate: this.getBirthday(),
            bio: this.state.bio,
            interests: this.state.interests,
            email: this.state.email,
            phone: this.state.phone,
            avatarId: this.state.imageId,
        };

        axios.put(url, data).then(() => {
            this.props.onCloseClick();
        });
    };

    saveUserPhoto = () => {
        const uploadUrl = apiUrl + '/storage/uploadFile/';
        const addImageUrl = apiUrl + '/images/';
        const requestTimeout = 30000;

        axios
            .post(uploadUrl, this.state.imageData, {
                timeout: requestTimeout,
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            })
            .then(response => {
                this.setState({
                    imageName: response.data,
                    imageData: {
                        filePath: response.data,
                    },
                });
            })
            .then(() => {
                axios
                    .post(addImageUrl, this.state.imageData, {
                        headers: {
                            'Content-Type': 'application/json',
                        },
                    })
                    .then(response => {
                        this.setState({
                            imageId: response.data.id,
                        });
                    });
            })
            .catch(error => {
                console.log(error);
            });
    };

    fetchData = (imageData, imageName) => {
        this.setState(
            {
                imageData: imageData,
                imageName: imageName,
            },
            () => {
                this.saveUserPhoto();
            },
        );
    };

    render() {
        const img =
            this.state.imageName !== null ? (
                <img src={getImageUrl(this.state.imageName)} alt='' className='imageStyle' />
            ) : (
                <img
                    src={'https://s3.ap-south-1.amazonaws.com/actent-res/1554136129708-default-user.png'}
                    alt=''
                    className='imageStyle'
                />
            );

        return (
            <div className='styleMainDiv'>
                <Card className='styleCard'>
                    <Typography component='h5' variant='h5' className='styleHeader' align='center'>
                        Edit profile
                    </Typography>

                    <div className='styleContainerEdit'>{img}</div>

                    <FileUpload fetchData={this.fetchData} handleSavePhoto={this.handleSavePhoto} />
                    <div className='styleName'>
                        <TextField
                            id='tv_first_name'
                            onChange={this.handleFirstName}
                            label='First Name:'
                            value={this.state.firstName}
                            fullWidth={true}
                        />
                        <TextField
                            id='tv_last_name'
                            onChange={this.handleLastName}
                            label='Last Name'
                            value={this.state.lastName}
                            fullWidth={true}
                        />
                        <TextField
                            id='tv_login'
                            label='Login'
                            onChange={this.handleLogin}
                            fullWidth={true}
                            rowsMax={3}
                            value={this.state.login}
                        />
                        {/* <TextField
                            id='tv_address'
                            label='Address'
                            onChange={this.handleAddress}
                            fullWidth={true}
                            multiline
                            rowsMax={3}
                            value={this.state.address !== null ? this.state.address.name : ''}
                        /> */}
                        <TextField
                            id='tv_phone'
                            label='Phone'
                            // onChange={this.handlePhone}
                            fullWidth={true}
                            error={!!this.state.errorPhone}
                            helperText={this.state.errorPhone}
                            value={this.state.phone !== null ? this.state.phone : ''}
                        />
                        <TextField
                            id='tv_email'
                            label='Email'
                            onChange={this.handleEmail}
                            fullWidth={true}
                            value={this.state.email}
                            error={!!this.state.errorEmail}
                            helperText={this.state.errorEmail}
                        />

                        <DatePicker
                            id='date_picker_birthday'
                            label='Birthday'
                            mode='landscape'
                            openToYearSelection={true}
                            fullWidth={true}
                            format={'yyyy-MM-dd'}
                            onChange={this.handleBirthday}
                            value={this.state.birthday}
                        />

                        <TextField
                            id='tv_bio'
                            label='Bio'
                            onChange={this.handleBio}
                            fullWidth={true}
                            multiline
                            rowsMax={3}
                            value={this.state.bio !== null ? this.state.bio : ''}
                            error={!!this.state.errorEmail}
                            helperText={this.state.errorEmail}
                        />
                    </div>
                    <div className='styleButtons'>
                        <Button label='Cancel' variant='contained' color='secondary' onClick={this.props.onCloseClick}>
                            Cancel{' '}
                        </Button>
                        <Button label='Save' color='primary' variant='contained' onClick={this.saveUserSettings}>
                            Save{' '}
                        </Button>
                    </div>
                </Card>
            </div>
        );
    }
}
