import React, { Component } from 'react';
import { saveAuthorizationToken, setAuthorizationHeader } from '../../util/apiUtils';


export default class OAuth2RedirectHandler extends Component {
    getUrlParameter(name) {
        name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
        let regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
        let results = regex.exec(this.props.location.search);
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    }

    render() {
        const token = this.getUrlParameter('token');

        if (token) {
            saveAuthorizationToken(token);
            setAuthorizationHeader();
            window.location.href = '/';
            this.NotificationManager.error('Invalid E-mail or Password!', 'Error!', 5000);
        } else {
            window.location.href = '/';
        }
        return <div />;
    }
}
