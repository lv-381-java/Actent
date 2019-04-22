import React, { Component } from 'react';
import { ACCESS_TOKEN } from '../../constants/apiConstants';
import { Redirect } from 'react-router-dom';

export default class OAuth2RedirectHandler extends Component {
    getUrlParameter(name) {
        name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
        console.log(name);
        let regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
        console.log(regex);
        let results = regex.exec(this.props.location.search);
        console.log(results);
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    }

    render() {
        const token = this.getUrlParameter('token');
        const error = this.getUrlParameter('error');

        if (token) {
            console.log(token);
            localStorage.setItem(ACCESS_TOKEN, token);
            return (
                <Redirect
                    to={{
                        pathname: '/profile',
                        state: { from: this.props.location },
                    }}
                />
            );
        } else {
            return (
                <Redirect
                    to={{
                        pathname: '/login',
                        state: {
                            from: this.props.location,
                            error: error,
                        },
                    }}
                />
            );
        }
    }
}
