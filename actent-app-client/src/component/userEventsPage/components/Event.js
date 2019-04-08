import React from 'react';
import {MDBJumbotron, MDBBtn, NavLink} from 'mdbreact';

const Event = props => {
    return (
        <MDBJumbotron>
            <div className='row'>
                <div className='col-md-2'>
                    <img
                        src='https://mdbootstrap.com/img/Photos/Slides/img%20(54).jpg'
                        className='rounded float-left img-thumbnail'
                        alt='some image'
                    />
                </div>
                <div className='col-md-10'>
                    <h2 className='h2 display-5'>{props.title}</h2>
                    <p className='lead'>{props.description}</p>
                    <div className='row'>
                        <div className='col-md-8'>
                            <NavLink to={`show/${props.eventId}`}>
                                <MDBBtn>Go to Event</MDBBtn>
                            </NavLink>
                        </div>
                    </div>
                </div>
            </div>
        </MDBJumbotron>
    );
};

export default Event;
