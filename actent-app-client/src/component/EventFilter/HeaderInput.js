import React, { Component } from 'react';
import { MDBCol } from 'mdbreact';
import axios from 'axios';

const style = {
    height: 'calc(1.5em + 1.75rem + 2px)',
};
export default class HeaderInput extends Component {
    handleChange = event => {
        let code = event.keyCode || event.which;
        this.props.setTitle(event.target.value, code);
    };

    render() {
        return (
            <MDBCol md='6'>
                <input
                    style={style}
                    value={this.props.showTitleName}
                    onChange={this.handleChange}
                    onKeyPress={this.handleChange}
                    className='form-control form-control-lg'
                    type='text'
                    placeholder='Find an event'
                    aria-label='Search'
                />
            </MDBCol>
        );
    }
}
