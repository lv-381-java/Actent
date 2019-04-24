import { Component } from 'react';
import { NavLink } from 'react-router-dom';
import {
    MDBNavbar,
    MDBNavbarBrand,
    MDBNavbarNav,
    MDBNavItem,
    MDBNavLink,
    MDBNavbarToggler,
    MDBCollapse,
    MDBDropdown,
    MDBDropdownToggle,
    MDBDropdownMenu,
    MDBDropdownItem,
    MDBIcon,
    PageLink,
} from 'mdbreact';
import React from 'react';
import { removeAuthorizationToken } from '../../util/apiUtils';
import { Link } from '@material-ui/core';

export default class Menu extends Component {

    constructor(props) {
        super(props);
        this.state = {
            collapse: false,
            isWideEnough: false,
        };
        this.onClick = this.onClick.bind(this);
    }

    onClick() {
        this.setState({
            collapse: !this.state.collapse,
        });
    }

    handleLogOut = () => {
        removeAuthorizationToken();
        window.location.href = "/"
    };

    render() {
        return (
            <MDBNavbar color='blue' dark expand='md'>
                <NavLink to='/'>
                    <MDBNavbarBrand>
                        <strong className='white-text'>Actent</strong>
                    </MDBNavbarBrand>
                </NavLink>
                <MDBNavbarToggler onClick={this.toggleCollapse} />
                <MDBCollapse id='navbarCollapse3' isOpen={this.state.isOpen} navbar>
                    <MDBNavbarNav left>
                        {this.props.firstName != undefined ?
                            <MDBNavItem>
                                <Link href='/createEvent' style={{ color: '#ffffff', textDecoration: 'none' }}>
                                    Create Event
                                </Link>
                            </MDBNavItem>
                            : null}
                    </MDBNavbarNav>
                    <MDBNavbarNav right>

                        {this.props.firstName != undefined ?
                            <MDBNavItem style={{alignSelf: 'center'}}>
                                <div style={{color: '#ffffff', marginRight: '10px'}}>
                                    Welcome, {this.props.firstName}
                                </div>
                            </MDBNavItem>
                            : null}

                        <MDBNavItem>
                            <MDBDropdown>
                                <MDBDropdownToggle nav caret>
                                    <MDBIcon icon='user' />
                                </MDBDropdownToggle>

                                {
                                    this.props.userId !== undefined ?
                                        <MDBDropdownMenu className='dropdown-default' right>
                                            <MDBDropdownItem href='/profile'>Profile</MDBDropdownItem>
                                            <MDBDropdownItem href='' onClick={this.handleLogOut}>
                                                Log Out
                                            </MDBDropdownItem>
                                        </MDBDropdownMenu>
                                        :
                                        <MDBDropdownMenu className='dropdown-default' right>
                                            <MDBDropdownItem href='/auth/signIn'>Sign In</MDBDropdownItem>
                                            <MDBDropdownItem href='/auth/signUp'>Sign Up</MDBDropdownItem>
                                        </MDBDropdownMenu>
                                }
                            </MDBDropdown>
                        </MDBNavItem>
                    </MDBNavbarNav>
                </MDBCollapse>
            </MDBNavbar>
        );
    }
}
