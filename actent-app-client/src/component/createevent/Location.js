import React from 'react';
import Select from 'react-select';
import axios from 'axios';

export default class Location
    extends React.Component {
    state = {
        address: {},
        locations: [],
    };

    componentDidMount() {
        this.getLocations();
    }

    handleChange = name => value => {
        this.setState({
            [name]: value,
        }, () => console.log("state " + this.state.address.value));

    };

    handleAddress = value => {
        this.setState({address: value}, () => {
            this.getLocations()
        });
    };

    getLocations = () => {
        if (this.state.address && this.state.address.length > 0) {
            let url = `http://localhost:8080/api/v1/locations/autocomplete/${this.state.address}`;

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
    handleAddLocation = () => {
        if (this.state.address.value && this.state.address.value.length > 0) {
            console.log("address    " + this.state.address.value)
            let url = `http://localhost:8080/api/v1/locations/byAddress/${this.state.address.value}`;

            axios.get(url)
                .then(response => {
                    const savedId = response.data;
                    console.log("return id " + savedId.id);
                    this.props.setLocationId(savedId.id);
                })
                .catch(function (error) {
                    console.log(error);
                });
        }
        ;
    };

    render() {
        return (
            <div className="form-group">
                <label>Location</label>
                <div className="selectStyle">
                    <Select
                        options={this.state.locations.map(location => ({
                            value: location.address,
                            label: location.address
                        }))}
                        value={this.state.address}
                        onChange={this.handleChange("address")}
                        placeholder="Enter location"
                        onInputChange={this.handleAddress}
                    />

                </div>
                <button
                    className={'btn btn-primary'}
                    onClick={this.handleAddLocation()}
                >
                    Save Location
                </button>
            </div>
        );
    }
}

