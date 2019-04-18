import React from 'react';
import Select from 'react-select';
import axios from 'axios';

export default class Location
    extends React.Component {
    state = {
        locations: []
    };

    componentDidMount() {
        this.getLocations();
    }

    getLocations = () => {
        if (this.props.address && this.props.address.length > 0) {
            let url = `http://localhost:8080/api/v1/locations/autocomplete/${this.props.address}`;

            axios.get(url)
                .then(response => {
                    const locations = response.data;
                    console.log(locations);
                    this.setState({locations: locations});
                })
                .catch(function (error) {
                    console.log(error);
                });
        }
        ;
    };

    handleChange = name => value => {
        // this.setState({
        //     address: value.value,
        //     isSet: true
        // });
        this.props.setAddress(value.value)

    };

    handleAddress = value => {
        if (value && value.length > 0) {
            this.props.setAddress(value)
        }

        // this.setState({address: value}, () => {
        //     this.getLocations()
        // });

    };


    handleAddLocation = () => {

        if (this.props.address && this.props.address.length > 0) {

            let url = `http://localhost:8080/api/v1/locations/byAddress/${this.props.address}`;

            axios.get(url)
                .then(response => {
                    const savedId = response.data;

                    this.props.setLocationId(savedId.id);
                })
                .catch(function (error) {
                    console.log(error);
                });
        }
        ;
    };

    shouldComponentUpdate(nextProps, nextState) {
        if (nextProps.address != this.props.address) {
            this.getLocations();
        }
        return true;
    }

    render() {
        console.log(this.props.address);
        return (
            <div className="form-group">
                <label>Location</label>
                <div className="selectStyle">
                    <Select
                        options={this.state.locations.map(location => ({
                            value: location.address,
                            label: location.address
                        }))}
                        value={this.props.address}
                        onChange={this.handleChange("address")}
                        placeholder={this.props.address ? this.props.address : "Enter location"}
                        onInputChange={this.handleAddress}
                    />

                </div>
                {this.props.address}
                <button
                    className={'btn btn-primary'}
                    onClick={this.handleAddLocation}
                >
                    Save Location
                </button>
            </div>
        );
    }
}

