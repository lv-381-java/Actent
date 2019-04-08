import React from 'react';
import { MDBCol } from 'mdbreact';

const style = {
    margin: 'auto',
};

export default class CityInput extends React.Component {
    state = {
        findCity: '',
    };
    handleChange = event => {
        this.setState({ findCity: event.target.value });
    };

    setCollorButton = () => {
        this.state.findCity === '' ? this.props.setButtonColor('info') : this.props.setButtonColor('success');
    };

    enterPressed = event => {
        let code = event.keyCode || event.which;
        if (code === 13) {
            this.setState({ findCity: event.target.value }, () => this.setCollorButton());
            this.props.setCity(this.state.findCity);

            console.log(this.state.findCity);
        }
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
                {console.log(this.props.cityName)}
                <input
                    className='form-control form-control-lg'
                    type='text'
                    name={this.state.findCity}
                    placeholder='Select city'
                    aria-label='Search'
                    onChange={this.handleChange}
                    onKeyPress={this.enterPressed}
                />
                {renderCity}
            </MDBCol>
        );
    }
}
