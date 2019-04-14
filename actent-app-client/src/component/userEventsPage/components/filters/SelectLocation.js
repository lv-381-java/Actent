import React from 'react';
import { MDBCol } from 'mdbreact';

export default class SelectLocation extends React.Component {
  setValue = event => {
        this.props.setLocation(event.target.value);
  };
  render() {
    return (
      <MDBCol>
        <input
          className='form-control form-control-lg'
          type='text'
          placeholder='name'
          aria-label='Search'
          onChange={this.setValue}
        />
      </MDBCol>
    );
  }
}
