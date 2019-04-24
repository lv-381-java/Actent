import React from 'react';

import './style.css';

import { MDBBtn } from 'mdbreact';

import axios from 'axios';

export default class SubscriptionsItem extends React.Component {
    deleteSubscription = () => {
        axios
            .delete(`/subscribers/${this.props.id}`)
            .then(() => this.props.getAllSubscription())
            .catch(function(error) {
                console.error(error);
            });
    };

    render() {
        return (
            <tr>
                <td>{this.props.category}</td>
                <td>{this.props.city}</td>
                <td>
                    <MDBBtn onClick={this.deleteSubscription} color='danger' size='sm'>
                        delete
                    </MDBBtn>
                </td>
            </tr>
        );
    }
}
