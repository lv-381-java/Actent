import React from "react";
import ReactDOM from "react-dom";
import axios from 'axios';
import Button from './Button';
import Input from './Input';

class Location extends React.Component {

    state = {
        country: undefined,
        countries: [],
        region: undefined,
        regions: [],
        city: undefined,
        cities: [],
        address: undefined,
        cityId: undefined,

        locationQueryStatus: undefined
    };

    handleAddress = (event) => {
        this.setState({address: event.target.value});
    };

    handleChangeCountries = (event) => {
        if (event.target.value === "None") {
            this.setState({
                country: undefined,
            });
        } else {
            this.setState({country: event.target.value}, () => (this.getRegions())
            );
        }
    }

    handleChangeRegions = (event) => {
        if (event.target.value === "None") {
            this.setState({region: undefined});
            // this.props.setCategoryId(undefined)
        } else {
            this.setState({region: event.target.value}, () => (this.getCities())
            );
        }
    }

    handleChangeCities = (event) => {
        if (event.target.value === "None") {
            this.setState({city: undefined});
            this.setCityId(undefined)
        } else {
            this.setState({city: event.target.value});
            this.setCityId(event.target.value)
        }
    }

    componentDidMount() {
        this.getCountries()
    };

    getCountries = () => {
        axios.get(`http://localhost:8080/api/v1/countries`)
            .then(res => {
                const count = res.data;
                this.setState({
                    countries: count,
                });
            })
    };

    getRegions = () => {
        console.log(`getRegions: ${this.state.region}`);
        axios.get(`http://localhost:8080/api/v1/regions?countryId=${this.state.country}`)
            .then(res => {
                console.log(res.data);
                const reg = res.data;
                this.setState({
                    regions: reg,
                });
            })
    };

    setCityId = (cityId) => {
        this.setState({cityId: cityId});
    }

    handleAddLocation = (e) => {
        let eventData = {
            address: this.state.address,
            cityId: this.state.cityId
        };

        this.setState({locationQueryStatus: 0});
        fetch("http://localhost:8080/api/v1/locations", {
            method: "POST",
            body: JSON.stringify(eventData),
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
            }
        })
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
                response.json()
                    .then(data => {
                        console.log("Successful" + data);
                        this.props.setLocationId(data.id);
                    });
            })

        ;
    }

    getCities = () => {
        console.log(`getR: ${this.state.city}`);
        axios.get(`http://localhost:8080/api/v1/cities?regionId=${this.state.region}`)
            .then(res => {
                console.log(res.data);
                const cit = res.data;
                this.setState({
                    cities: cit,
                });
            })
    };

    render() {
        return (
            <div className="form-group">
                <label>Country</label>
                <div className="selectStyle">
                    <div>
                        <select className="browser-default custom-select" onChange={this.handleChangeCountries}
                                value={this.state.country}>
                            <option key="None" value="None"></option>
                            {this.state.countries.map(a => {
                                    return (
                                        <option key={a.id} value={a.id}>{a.name}</option>
                                    )
                                }
                            )
                            }
                        </select>
                    </div>

                    {this.state.country && (<div>
                            <label>Region</label>
                            <select className="browser-default custom-select" onChange={this.handleChangeRegions}
                                    value={this.state.region}>
                                <option key="None" value="None"></option>
                                {this.state.regions.map(a => {
                                        return (
                                            <option key={a.id} value={a.id}>{a.name}</option>
                                        )
                                    }
                                )
                                }
                            </select>

                        </div>
                    )}
                    {this.state.region && (<div>
                            <label>City</label>
                            <select className="browser-default custom-select" onChange={this.handleChangeCities}
                                    value={this.state.city}>
                                <option key="None" value="None"></option>
                                {this.state.cities.map(a => {
                                        return (
                                            <option key={a.id} value={a.id}>{a.name}</option>
                                        )
                                    }
                                )
                                }
                            </select>
                        </div>
                    )}
                </div>
                {this.state.city && (<div>
                        <Input title="To continue please enter address and press Save Location button" type="text"
                               placeholder="Please enter address of event" handleChange={this.handleAddress}/>
                        {this.state.locationQueryStatus === 0 && (<div>Sending request...</div>)}
                        {this.state.locationQueryStatus === 1 && (<div>Location created successfully</div>)}
                        {this.state.locationQueryStatus === 2 && (<div>Something went wrong.....</div>)}
                        <Button
                            action={this.handleAddLocation}
                            type={"primary"}
                            title={"Save Location"}
                            style={buttonStyle}
                        />{" "}
                    </div>
                )}
            </div>
        );
    }
}

const buttonStyle = {
    margin: "10px 10px 10px 10px"
};

export default Location;