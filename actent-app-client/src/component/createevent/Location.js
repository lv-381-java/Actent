import React from 'react';
import Select from 'react-select';
import axios from 'axios';

export default class Location
    extends React.Component {
    state = {
        locations: [],
        locationQueryStatus: undefined
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
          this.props.setAddress(value.value)
    };

    handleAddress = value => {
        if (value && value.length > 0) {
            this.props.setAddress(value)
        }
    };


    handleAddLocation = () => {

        if (this.props.address && this.props.address.length > 0) {

            let url = `http://localhost:8080/api/v1/locations/byAddress/${this.props.address}`;

            axios.get(url)
                .then((response) => {
                    let status = +response.status;
                    if (status >= 200 && status < 300) {
                        this.setState({locationQueryStatus: 1});
                    } else {
                        this.setState({locationQueryStatus: 2});
                    }
                    return response;
                }, (err) => {
                    console.log('error', err);
                    this.setState({locationQueryStatus: 2});
                    return JSON.stringify({});
                })
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
                        placeholder={this.props.address ? this.props.address : "To continue please enter address and press Save Location"}
                        onInputChange={this.handleAddress}
                    />
                    <span>{this.props.errorMessage}</span>
                </div>
                {this.state.locationQueryStatus === 0 && (<div>Sending request...</div>)}
                {this.state.locationQueryStatus === 1 && (<div>Location created successfully</div>)}
                {this.state.locationQueryStatus === 2 && (<div>Something went wrong.....</div>)}
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

