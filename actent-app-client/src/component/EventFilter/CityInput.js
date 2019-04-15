import React from 'react';
import { MDBCol } from 'mdbreact';

const style = {
    margin: 'auto',
};

export default class CityInput extends React.Component {
    state = {
        findCity: '',
        keyCode: 0,
    };

    handleChange = event => {
        let code = event.keyCode || event.which;
        console.log(code);
        this.props.setCity(event.target.value, code);
    };

    setCollorButton = () => {
        this.state.findCity === '' ? this.props.setButtonColor('info') : this.props.setButtonColor('success');
    };

    render() {
        const renderCity =
            this.props.cityName === undefined || this.props.cityName === '' ? null : (
                <div style={{ paddingTop: '10px' }}>
                    <h3>Selected city: {this.props.cityName}</h3>
                </div>
            );
        return (
            <MDBCol md='6' style={style}>
                <input
                    className='form-control form-control-lg'
                    type='text'
                    placeholder='Select city'
                    value={this.props.showCityName}
                    aria-label='Search'
                    onChange={this.handleChange}
                    onKeyPress={this.handleChange}
                />
                {renderCity}
            </MDBCol>
        );
    }
}
