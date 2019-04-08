import React from 'react';
import Button from '@material-ui/core/Button';
import './style.css';
import { Redirect } from 'react-router-dom';

export const s3Root = 'https://s3.ap-south-1.amazonaws.com/';

export function getImageUrl(imageName) {
    if (imageName) {
        return s3Root + 'actent-res/' + imageName;
    }
}

export default class ProfileView extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        const editBtn = this.props.isMyProfile ? (
            <Button
                style={{ marginLeft: '20px' }}
                label='Edit'
                color='primary'
                variant='contained'
                disabled={this.props.profileData.id === ''}
                onClick={this.props.onEditClick}
            >
                Edit
            </Button>
        ) : (
            <Button
                label='Add review'
                color='secondary'
                variant='contained'
                disabled={this.props.profileData.id === ''}
                href={`${this.props.link}`}
            >
                Add review
            </Button>
        );

        const userEvents = this.props.isMyProfile ? (
            <Button label='My events' color='primary' variant='contained' disabled={this.props.profileData.id === ''}>
                My Events
            </Button>
        ) : null;

        const img =
            this.props.profileData.avatar !== null ? (
                <img src={getImageUrl(this.props.profileData.avatar.filePath)} alt='' className='imageStyle' />
            ) : (
                <img
                    src={'https://s3.ap-south-1.amazonaws.com/actent-res/1554136129708-default-user.png'}
                    alt=''
                    className='imageStyle'
                />
            );

        return (
            <div className='styleMain'>
                <div className='styleLowerMain1'>
                    <div className='styleContainer'>{img}</div>

                    <div>
                        <p className='styleInput'>
                            <span className='styleSpan'>First Name:</span>
                            {this.props.profileData.firstName}
                        </p>
                        <p className='styleInput'>
                            <span className='styleSpan'>Last Name:</span>
                            {this.props.profileData.lastName}
                        </p>
                        <p className='styleInput'>
                            <span className='styleSpan'>Login:</span>
                            {this.props.profileData.login}
                        </p>
                        <p className='styleInput'>
                            <span className='styleSpan'>Email:</span>
                            {this.props.profileData.email}
                        </p>
                        <p className='styleInput'>
                            <span className='styleSpan'>Phone:</span>
                            {this.props.profileData.phone}
                        </p>
                        <p className='styleInput'>
                            <span className='styleSpan'>Address:</span>
                            {this.props.address !== null && this.props.address !== undefined
                                ? `${this.props.profileData.address.name} , ${
                                      this.props.profileData.address.regionCountryName
                                  }`
                                : null}
                        </p>
                        <p className='styleInput'>
                            <span className='styleSpan'>Birth Date:</span>
                            {this.props.profileData.birthday}
                        </p>
                        <p className='styleInput'>
                            <span className='styleSpan'>Bio:</span>
                            {this.props.profileData.bio}
                        </p>
                    </div>
                </div>

                <div className='styleLowerMain2'>
                    {userEvents} {editBtn}
                </div>
            </div>
        );
    }
}
