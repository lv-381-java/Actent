import React from 'react';
import axios from 'axios';
import ProfileView from './ProfileView';
import ProfileEdit from './ProfileEdit';
import { getCurrentUser } from '../../util/apiUtils';
import { Redirect } from 'react-router-dom';

export const apiUrl = 'http://localhost:8080/api/v1';

export default class Profile extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            userId: Number(props.match.params.id),
            isEdit: false,
            isReviewing: false,
            isUserEventsOpen: false,
            isMyProfile: props.current ? true : false,
            firstName: '',
            lastName: '',
            phone: '',
            email: '',
            login: '',
            address: '',
            birthday: '',
            bio: '',
            interests: '',
            avatar: '',
        };
    }

    // componentWillReceiveProps(nextProps, nextContext) {
    //     //TODO: check if my profile
    //     this.getProfile();
    // }

    componentDidMount() {
        this.getProfile();
    }

    getProfile = () => {
        const profileUrl = apiUrl + '/users/' + this.state.userId;

        axios
            .get(profileUrl)
            .then(response => {
                this.setState({
                    userId: response.data['id'],
                    firstName: response.data['firstName'],
                    lastName: response.data['lastName'],
                    login: response.data['login'],
                    address: response.data['location'],
                    birthday: response.data['birthDate'],
                    bio: response.data['bio'],
                    interests: response.data['interests'],
                    avatonAddReviewClick: response.data['avatar'],
                    email: response.data['email'],
                    phone: response.data['phone'],
                });
            })
            .catch(function(error) {
                console.log(error);
            });
    };

    handleEditClick = () => {
        this.setState({
            isEdit: true,
        });
    };

    handleAddReview = () => {
        this.setState({
            isReviewing: true,
        });

        this.setState({
            redirect: true,
        });
        return <Redirect to={`/users/${this.state.currentUserId}/addReview`} />;
    };

    handleClose = () => {
        this.setState({
            isEdit: false,
        });
        window.location.reload();
    };

    handleUserEvents = () => {
        this.setState({
            isUserEventsOpen: true,
        });
    };

    render() {
        const profileData = {
            userId: this.state.userId,
            firstName: this.state.firstName,
            lastName: this.state.lastName,
            phone: this.state.phone,
            email: this.state.email,
            login: this.state.login,
            address: this.state.address,
            birthday: this.state.birthday,
            bio: this.state.bio,
            interests: this.state.interests,
            avatar: this.state.avatar,
        };

        const view = this.state.isEdit ? (
            <ProfileEdit profileData={profileData} onCloseClick={this.handleClose} />
        ) : (
            <ProfileView
                profileData={profileData}
                isMyProfile={this.state.isMyProfile}
                onEditClick={this.handleEditClick}
                onAddReviewClick={this.handleAddReview}
                link={`/addReview/${this.state.userId}`}
            />
        );

        return view;
    }
}
