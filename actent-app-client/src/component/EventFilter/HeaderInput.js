import React, { Component } from 'react';
import { MDBCol } from 'mdbreact';
import axios from 'axios';

const style = {
    height: 'calc(1.5em + 1.75rem + 2px)',
};
export default class HeaderInput extends Component {
    state = {
        findEvent: this.props.findEvent,
    };

    handleChange = event => {
        this.setState({ findEvent: event.target.value });
    };

    enterPressed = event => {
        let code = event.keyCode || event.which;
        console.log(code);
        if (code === 13) {
            this.props.setTitle(this.state.findEvent);
        }
    };

    render() {
        return (
            <MDBCol md='6'>
                <input
                    style={style}
                    name='findEvent'
                    onChange={this.handleChange}
                    onKeyPress={this.enterPressed}
                    className='form-control form-control-lg'
                    type='text'
                    placeholder='Find an event'
                    aria-label='Search'
                />
            </MDBCol>
        );
    }
}
