import React, {Component} from "react";

import Input from "./Input";
import TextArea from "./TextArea";
import Select from "./Select";
import Button from "./Button";
import Category from "./Category";
import Location from "./Location";
import TextField from '@material-ui/core/TextField';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.min.js';
import './styles.css';
import {getCurrentUser} from "../../util/apiUtils";
import FileUpload from "../profile/FileUpload";
import axios from "axios";
import {getImageUrl} from "../profile/ProfileView";
import {API_BASE_URL} from "../../constants/apiConstants";


class FormContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            accessType: "",
            capacity: "",
            categoryId: undefined,
            creatorId: undefined,
            description: "",
            duration: "",
            imageId: "",
            locationId: undefined,
            startDate: "",
            title: "",
            accessOptions: ["public", "private"],
            errorTitle: undefined,
            errorDescription: undefined,
            errorCapacity: undefined,
            errorCategory: undefined,
            errorLocation: undefined,
            errorAccessType: undefined,
            errorStartDate: "",
            errorDuration: "",
            formQueryStatus: undefined,
            imageName: "",
            imageData: {},
            filePath: undefined,
            address: "",
            locations: [],
        };
    }

    async componentDidMount() {
        try {
            const data = (await getCurrentUser()).data;
            this.setState({
                ...this.state,
                creatorId: data.id,
            });
        } catch (e) {
            console.error(e);
        }
    }

    handleTitle = (e) => {
        const value = e.target.value;
        this.setState({title: value});
    };

    handleInput = (e) => {
        let value = e.target.value;
        let name = e.target.name;
        this.setState({[name]: value});
    };

    handleDateChange = e => {
        let value = e.target.value;
        this.setState({startDate: value});
    };

    handleDuration = (e) => {
        let value = e.target.value;
        value = value.split(':');
        let h = 1000 * 60 * 60 * (+value[0]);
        let m = 1000 * 60 * (+value[1]);
        this.setState({duration: h + m});
    };

    handleTextArea = (e) => {
        console.log("Inside handleTextArea");
        let value = e.target.value;
        this.setState({description: value});
    };
    isTitleValid = (title) => {
        let errorTitle = "";
        if (title === '') {
            errorTitle = "This field shouldn't be empty";
            this.setState({errorTitle});
            return false;
        }
        if (title.length < 3 || title.length > 100) {
            errorTitle = "Title must be at least 3 and not more 100 characters";
            this.setState({errorTitle});
            return false;
        }
        this.setState({errorTitle});
        return true;
    };

    isStartDateValid = (startDate) => {
        let errorStartDate = "";
        if (startDate === '') {
            errorStartDate = "Incorrect date format";
            this.setState({errorStartDate});
            return false;
        }
        if (Date.parse(startDate) < Date.now()) {
            errorStartDate = "Start date cannot be past";
            this.setState({errorStartDate});
            return false;
        }
        this.setState({errorStartDate});
        return true;
    };

    isCapacityValid = (capacity) => {
        let errorCapacity = "";
        if (capacity === '') {
            errorCapacity = "This field shouldn't be empty";
            this.setState({errorCapacity});
            return false;
        }
        if (capacity < 1) {
            errorCapacity = "Capacity must be greater than 0";
            this.setState({errorCapacity});
            return false;
        }
        this.setState({errorCapacity});
        return true;
    };

    isCategoryValid = (category) => {
        let errorCategory = "";
        if (!category) {
            errorCategory = "This field shouldn't be empty";
            this.setState({errorCategory});
            return false;
        }
        this.setState({errorCategory});
        return true;
    };

    isLocationValid = (location) => {
        let errorLocation = "";
        if (!location) {
            errorLocation = "This field shouldn't be empty";
            this.setState({errorLocation});
            return false;
        }
        this.setState({errorLocation});
        return true;
    };

    isAccessTypeValid = (accessType) => {
        let errorAccessType = "";
        if (!accessType) {
            errorAccessType = "This field shouldn't be empty";
            this.setState({errorAccessType});
            return false;
        }
        this.setState({errorAccessType});
        return true;
    };


    isDescriptionValid = (description) => {
        let errorDescription = "";
        if (description === '') {
            errorDescription = "This field shouldn't be empty";
            this.setState({errorDescription});
            return false;
        }

        if (description.length < 3 || description.length > 850) {
            errorDescription = "Description must be at least 3 and not more 850 characters";
            this.setState({errorDescription});
            return false;
        }
        this.setState({errorDescription});
        return true;
    };

    isDurationValid = (duration) => {
        let errorDuration = "";
        if (!duration) {
            errorDuration = "Wrong duration format. Must be hh:mm";
            this.setState({errorDuration});
            return false;
        }
        this.setState({errorDuration});
        return true;
    };

    isFormValid = () => {
        return this.isTitleValid(this.state.title)
            && this.isCapacityValid(this.state.capacity)
            && this.isDurationValid(this.state.duration)
            && this.isCategoryValid(this.state.categoryId)
            && this.isStartDateValid(this.state.startDate)
            && this.isAccessTypeValid(this.state.accessType)
            && this.isDescriptionValid(this.state.description)
            ;
    };

    formSubmit = () => {
        this.setState({
            formQueryStatus: 0
        });

        if (this.state.address && this.state.address.length > 0) {
            let url = API_BASE_URL + `/locations/byAddress/${this.state.address}`;
            axios.get(url)
                .then(response => {
                    const data = response.data;
                    this.setState({
                        locationId: data.id,
                        formQueryStatus: 0
                    }, this.handleFormSubmit);

                    return data.id;
                })
                .then(locationId => {
                    this.isLocationValid(locationId);
                })
                .catch(function (error) {
                    console.log(error);
                });
        } else {
            this.isLocationValid();
            this.setState({
                formQueryStatus: 2
            });
        }
    };

    handleFormSubmit = () => {

        let eventData = this.state;

        if (!this.isFormValid()) {
            this.setState({
                formQueryStatus: 2
            });
            return {};
        }

        fetch(API_BASE_URL + "/events", {
            method: "POST",
            body: JSON.stringify(eventData),
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
            }
        })
            .then(response => {
                let status = +response.status;
                if (status >= 200 && status < 300) {
                    this.setState({formQueryStatus: 1});
                } else {
                    this.setState({formQueryStatus: 2});
                }
                return response;
            })
            .then(response => {
                response.json()
                    .then(data => {
                        console.log("Successful" + data);
                    });
            });
    };

    handleClearForm = (e) => {
        e.preventDefault();
        this.setState({
            accessType: "",
            capacity: "",
            categoryId: "",
            description: "",
            duration: "",
            locationId: "",
            title: "",
        });
    };
    setCategoryId = (categoryId) => {
        this.setState({categoryId: categoryId, hasChildCategory: true});
    };

    setLocationId = (locationId) => {
        this.setState({locationId: locationId});
    };
    saveUserPhoto = () => {
        const uploadUrl = API_BASE_URL + '/storage/uploadFile/';
        const addImageUrl = API_BASE_URL + '/images/';
        const requestTimeout = 30000;
        axios
            .post(uploadUrl, this.state.imageData, {
                timeout: requestTimeout,
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            })
            .then(response => {
                this.setState({
                    imageName: response.data,
                    imageData: {
                        filePath: response.data,
                    },
                });
            })
            .then(() => {
                axios
                    .post(addImageUrl, this.state.imageData, {
                        headers: {
                            'Content-Type': 'application/json',
                        },
                    })
                    .then(response => {
                        this.setState({
                            imageId: response.data.id
                        });
                    });
            })
            .catch(error => {
                console.log(error);
            });
    };

    fetchData = (imageData, imageName) => {
        this.setState(
            {
                imageData: imageData,
                imageName: imageName,
            },
            () => {
                this.saveUserPhoto();
            },
        );
    };

    setAddress = (address) => {
        this.setState({address: address});
    }

    render() {
        return (
            <div className="mainWrapper">
                <h1> Creating an event </h1>
                <Input
                    type={"text"}
                    title={"Title "}
                    name={"title"}
                    value={this.state.title}
                    placeholder={"Enter event title"}
                    handleChange={this.handleTitle}
                    errorMessage={this.state.errorTitle}
                />

                <Input
                    type={"number"}
                    title={"Capacity "}
                    name={"capacity"}
                    min={1}
                    max={100}
                    value={this.state.capacity}
                    placeholder={"Enter capacity of event"}
                    handleChange={this.handleInput}
                    errorMessage={this.state.errorCapacity}
                />
                <p>Duration(hh:mm)</p>
                <TextField
                    id="time"
                    name={"Duration(hh:mm)"}
                    type="time"
                    defaultValue=""
                    onChange={this.handleDuration}
                    InputLabelProps={{
                        shrink: true,
                    }}
                    inputProps={{
                        step: 900, // 15 min
                    }}
                />
                <p></p>
                <span>{this.state.errorDuration}</span>
                <Category
                    value={this.state.categoryId}
                    setCategoryId={this.setCategoryId}
                    errorMessage={this.state.errorCategory}
                />
                <Location
                    value={this.state.locationId}
                    setLocationId={this.setLocationId}
                    address={this.state.address}
                    setAddress={this.setAddress}
                    errorMessage={this.state.errorLocation}
                />
                <div>Start Date</div>
                <TextField
                    id="datetime-local"
                    type="datetime-local"
                    onChange={this.handleDateChange}
                    defaultValue=""
                    InputLabelProps={{
                        shrink: true,
                    }}
                />
                <p></p>
                <span>{this.state.errorStartDate}</span>

                <Select title={'Access'}
                        name={'accessType'}
                        options={this.state.accessOptions}
                        value={this.state.accessType}
                        placeholder={'Select access type of event'}
                        handleChange={this.handleInput}
                        errorMessage={this.state.errorAccessType}
                />
                <TextArea
                    title={"Description "}
                    rows={10}
                    value={this.state.description}
                    name={"description"}
                    handleChange={this.handleTextArea}
                    placeholder={"Describe details about event"}
                    errorMessage={this.state.errorDescription}
                />
                <div className="imagePreview">
                    <img src={getImageUrl(this.state.imageName)}/>
                </div>
                <FileUpload fetchData={this.fetchData} handleSavePhoto={this.handleSavePhoto}/>

                {this.state.formQueryStatus === 0 && (<div>Status: Sending request...</div>)}
                {this.state.formQueryStatus === 1 && (<div>Status: Event created successfully</div>)}
                {this.state.formQueryStatus === 2 && (<div>Status: Something went wrong.....</div>)}
                <Button
                    action={this.formSubmit}
                    type={"primary"}
                    title={"Submit"}
                    style={buttonStyle}
                />
                <Button
                    action={this.handleClearForm}
                    type={"secondary"}
                    title={"Clear"}
                    style={buttonStyle}
                />
            </div>
        );
    }
}

const buttonStyle = {
    margin: "10px 10px 10px 10px"
};

export default FormContainer;





