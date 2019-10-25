import React from 'react';
import Button from '@material-ui/core/Button';
import './style.css';
import { MDBTable, MDBTableBody, MDBTableHead, MDBCol, MDBCollapse } from 'mdbreact';
import { NavLink } from 'react-router-dom';
import axios from 'axios';
import SubscriptionsItem from './subscriptionsItem';
import {AMAZON_S3_URL} from '../../constants/apiConstants'

export function getImageUrl(imageName) {
    if (imageName) {
        return AMAZON_S3_URL + imageName;
    }
}

export default class ProfileView extends React.Component {
    constructor(props) {
        super(props);
    }

    state = {
        colapse: '',
        subscriptions: [],
    };

    componentDidMount() {
        this.getAllSubscription();
    }

    getAllSubscription = () => {
        axios
            .get(`/getSubscriptions`)
            .then(res => {
                let subscriptions = res.data;
                this.setState({ subscriptions: subscriptions });
            })
            .catch(function(error) {
                console.error(error);
            });
    };
    deleteSubscription = id => {
        axios.get(`/subscribers/${id}`).catch(function(error) {
            console.error(error);
        });
    };

    toggleCollapse = collapseID => () => {
        let colapse = this.state.colapse;

        colapse === collapseID ? this.setState({ colapse: '' }) : this.setState({ colapse: collapseID });
    };
    deleteSubcription = event => {
        let id = Object.assign({}, event.target);
        console.log(id);
        this.deleteSubcription(id);
        console.log(id);
    };

    render() {
        let subscriptions = this.state.subscriptions;
        const editBtn = this.props.isMyProfile ? (
            <Button
                style={{ marginLeft: '20px' }}
                label='Edit'
                color='primary'
                variant='contained'
                disabled={this.props.profileData.id === ''}
                onClick={this.props.onEditClick}>
                Edit
            </Button>
        ) : (
            <Button
                label='Add review'
                color='secondary'
                variant='contained'
                disabled={this.props.profileData.id === ''}
                href={`${this.props.link}`}>
                Add review
            </Button>
        );
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

                        {this.props.profileData.address != null && this.props.profileData.address != undefined ? (
                            <p className='styleInput'>
                                <span className='styleSpan'>Address:</span>
                                {this.props.profileData.address.address}
                            </p>
                        ) : (
                            console.log('address is null')
                        )}

                        {/*<p className='styleInput'>*/}
                        {/*<span className='styleSpan'>Address:</span>*/}
                        {/*{this.props.profileData.address.address}*/}
                        {/*</p>*/}
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
                    {editBtn}
                    <Button
                        label='My events'
                        color='primary'
                        variant='contained'
                        onClick={this.toggleCollapse('subscriptions')}>
                        My Subscriptions
                    </Button>
                    <NavLink to={`/userEvents/${this.props.userId}`}>
                        <Button label='Events' color='primary' variant='contained'>
                            {this.props.isMyProfile ? 'My events' : 'Events'}
                        </Button>
                    </NavLink>
                </div>
                <div className='styleLowerMain2'>
                    <MDBCol style={{ maxWidth: '100%' }}>
                        <MDBCollapse id='subscriptions' isOpen={this.state.colapse}>
                            <MDBTable hover btn>
                                <MDBTableHead>
                                    <tr>
                                        <th>category</th>
                                        <th>location</th>
                                        <th>delete</th>
                                    </tr>
                                </MDBTableHead>
                                <MDBTableBody>
                                    {subscriptions.map(subscription => {
                                        return (
                                            <SubscriptionsItem
                                                getAllSubscription={this.getAllSubscription}
                                                key={`${subscription.id}`}
                                                category={subscription.category}
                                                city={subscription.city}
                                                id={subscription.id}
                                            />
                                        );
                                    })}
                                </MDBTableBody>
                            </MDBTable>
                        </MDBCollapse>
                    </MDBCol>
                </div>
            </div>
        );
    }
}
