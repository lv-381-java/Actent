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

    handleLogOut = _ => {
        removeAuthorizationToken();
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
                        <MDBNavItem>
                            <Link href='/createEvent' style={{ color: '#ffffff', textDecoration: 'none' }}>
                                Create Event
                            </Link>
                        </MDBNavItem>
                        <MDBNavItem>
                            <Link href='/chat' style={{ color: '#ffffff', textDecoration: 'none', marginLeft: '25%' }}>
                                Chat
                            </Link>
                        </MDBNavItem>
                    </MDBNavbarNav>
                    <MDBNavbarNav right>
                        <MDBNavItem>
                            <MDBDropdown>
                                <MDBDropdownToggle nav caret>
                                    <MDBIcon icon='user' />
                                </MDBDropdownToggle>
                                <MDBDropdownMenu className='dropdown-default' right>
                                    <MDBDropdownItem href='/auth/signIn'>Sign In</MDBDropdownItem>
                                    <MDBDropdownItem href='/auth/signUp'>Sign Up</MDBDropdownItem>
                                    <MDBDropdownItem href='/profile'>Profile</MDBDropdownItem>
                                    <MDBDropdownItem href='/' onClick={this.handleLogOut}>
                                        Log Out
                                    </MDBDropdownItem>
                                </MDBDropdownMenu>
                            </MDBDropdown>
                        </MDBNavItem>
                    </MDBNavbarNav>
                </MDBCollapse>
            </MDBNavbar>
        );
    }
}
