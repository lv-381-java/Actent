import React, { Component } from 'react';
import { MDBBtn, MDBCard, MDBCardBody, MDBCardImage, MDBCardTitle, MDBCardText, MDBCol } from 'mdbreact';
import { Route, Link, NavLink, BrowserRouter } from 'react-router-dom';

export default class CardExample extends React.Component {
    render() {
        const img =
            this.props.image !== undefined ? (
                <MDBCardImage
                    style={{ cursor: 'pointer' }}
                    className='img-fluid'
                    src={`https://s3.ap-south-1.amazonaws.com/actent-res/${this.props.image}`}
                    waves
                />
            ) : (
                <MDBCardImage
                    style={{ cursor: 'pointer' }}
                    className='img-fluid'
                    src={`https://mdbootstrap.com/img/Mockups/Lightbox/Thumbnail/img%20(67).jpg`}
                    waves
                />
            );
        return (
            <MDBCol style={{ maxWidth: '22rem', margin: 'auto' }}>
                <MDBCard>
                    <NavLink to={`show/${this.props.eventId}`}>{img}</NavLink>
                    <MDBCardBody>
                        <MDBCardTitle>{this.props.title}</MDBCardTitle>
                        <MDBCardText style={{ whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>Description: {this.props.description}</MDBCardText>
                        <MDBCardText>Location: {this.props.city}</MDBCardText>
                        <MDBCardText>Category: {this.props.category}</MDBCardText>
                        <NavLink to={`show/${this.props.eventId}`}>
                            <MDBBtn>Go to Event</MDBBtn>
                        </NavLink>
                    </MDBCardBody>
                </MDBCard>
            </MDBCol>
        );
    }
}
