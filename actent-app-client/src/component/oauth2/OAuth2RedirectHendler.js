import React, { Component } from 'react';
import { ACCESS_TOKEN } from '../../constants/apiConstants';
import { Redirect } from 'react-router-dom';
import { saveAuthorizationToken, setAuthorizationHeader, getTokenFromCredentials } from '../../util/apiUtils';

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
            saveAuthorizationToken(token);
            setAuthorizationHeader();
            window.location.href = "/";

        } else {
            window.location.href = "/";
        }
        return(<div></div>);

    }
}
