import React from 'react';
import Select from 'react-select';
import axios from 'axios';
import {MDBIcon} from "mdbreact";
import Link from '@material-ui/core/Link';

export default class Location
    extends React.Component {
    state = {
        locations: [],
        address: "",
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
        this.setState({
            address: value.value,
            isSet: true
        });
        this.props.setAddress(value.value)

    };

    handleAddress = value => {
        if (value && value.length > 0) {
            this.props.setAddress(value)
        }

        this.setState({address: value}, () => {
            this.getLocations()
        });
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
                <div className="selectStyle address">
                    {
                        this.props.address !== ""
                        && (<Link rel="noopener"
                                  target="_blank"
                                  href={`https://www.google.com/maps/place/${this.props.address}`}>
                            <MDBIcon icon="map" size="2x"/>
                        </Link>)
                    }
                    <Select className="find_addres_fild"
                            options={this.state.locations.map(location => ({
                                value: location.address,
                                label: location.address
                            }))}
                            value={this.state.address}
                            onChange={this.handleChange("address")}
                            placeholder={this.props.address ? this.props.address : "To continue please enter address and press Save Location"}
                            onInputChange={this.handleAddress}
                    />
                </div>
                <span>{this.props.errorMessage}</span>
                {this.state.locationQueryStatus === 0 && (<div>Sending request...</div>)}
                {this.state.locationQueryStatus === 1 && (<div>Location created successfully</div>)}
                {this.state.locationQueryStatus === 2 && (<div>Something went wrong.....</div>)}
            </div>
        );
    }
}

