import React from 'react';
import { MDBBtn, MDBCard, MDBCardBody, MDBCardImage, MDBCardTitle, MDBCardText, MDBCol } from 'mdbreact';
import { Route, Link, NavLink, BrowserRouter } from 'react-router-dom';

const CardExample = props => {
    return (
        <MDBCol style={{ maxWidth: '22rem', margin: 'auto' }}>
            <MDBCard>
                <NavLink to={`show/${props.eventId}`}>
                    <MDBCardImage
                        style={{ cursor: 'pointer' }}
                        className='img-fluid'
                        src='https://mdbootstrap.com/img/Mockups/Lightbox/Thumbnail/img%20(67).jpg'
                        waves
                    />
                </NavLink>
                <MDBCardBody>
                    <MDBCardTitle>{props.title}</MDBCardTitle>
                    <MDBCardText>Description: {props.description}</MDBCardText>
                    <MDBCardText>City: {props.city}</MDBCardText>
                    <MDBCardText>Category: {props.category}</MDBCardText>
                    <NavLink to={`show/${props.eventId}`}>
                        <MDBBtn>Go to Event</MDBBtn>
                    </NavLink>
                </MDBCardBody>
            </MDBCard>
        </MDBCol>
    );
};

export default CardExample;
