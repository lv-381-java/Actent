import React, {Component} from "react";

import Input from "./Input";
import TextArea from "./TextArea";
import Select from "./Select";
import Button from "./Button";
import Category from "./Category";
import {DatePicker, MuiPickersUtilsProvider, TimePicker} from "material-ui-pickers";
import DateFnsUtils from "@date-io/date-fns";
import Location from "./Location";
import TextField from '@material-ui/core/TextField';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.min.js';
import './styles.css';
import {getCurrentUser} from "../../util/apiUtils";


class FormContainer extends Component {
    constructor(props) {
        super(props);

        this.state = {
            accessType: undefined,
            capacity: undefined,
            categoryId: undefined,
            creatorId: undefined,
            description: undefined,
            duration: undefined,
            imageId: null,
            locationId: undefined,
            startDate: '2019-04-04T21:11:54',
            title: undefined,
            accessOptions: ["public", "private"],
            errorTitle: undefined,
            errorDescription: undefined,
            formQueryStatus: undefined
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
    }

    handleInput = (e) => {
        let value = e.target.value;
        let name = e.target.name;
        this.setState({[name]: value});
    }

    handleDateChange = (e) => {
        // let value = Date.parse(e);
        let value = e.target.value;
        this.setState({startDate: value});
    }

    handleDuration = (e) => {
        let value = e.target.value;
        value = value.split(':');
        let h = 1000 * 60 * 60 * (+value[0]);
        let m = 1000 * 60 * (+value[1]);
        this.setState({duration: h + m});
    }

    handleTextArea = (e) => {
        console.log("Inside handleTextArea");
        let value = e.target.value;
        this.setState({description: value});
    }

    handleFormSubmit = (e) => {
        e.preventDefault();

        if (!this.isFormValid()) return;

        let eventData = this.state;

        this.setState({
            formQueryStatus: 0
        });
        fetch("http://localhost:8080/api/v1/events", {
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
                response.json().then(data => {
                    console.log("Successful" + data);
                });
            });
    }

    isTitleValid = (title) => {
        let isValid = true;
        let errorTitle = "";
        if (title === '') {
            errorTitle = "This fiels shouldn't be empty";
            isValid = false;
        }

        if (title.length < 3) {
            errorTitle = "Min 3 symbols";
            isValid = false;
        }
        this.setState({errorTitle});
        return isValid;
    }

    isDescriptionValid = (description) => {
        let isValid = true;
        let errorDescription = "";
        if (description === '') {
            errorDescription = "This fiels shouldn't be empty";
            isValid = false;
        }

        if (description.length < 10) {
            errorDescription = "Min 10 symbols";
            isValid = false;
        }
        this.setState({errorDescription});
        return isValid;
    }

    isFormValid = () => {
        return this.isTitleValid(this.state.title) && this.isDescriptionValid(this.state.description);
    }

    handleClearForm = (e) => {
        e.preventDefault();
        this.setState({
            accessType: "",
            capacity: "",
            categoryId: "",
            creatorId: 1,
            description: "",
            duration: "",
            locationId: "",
            title: "",
        });
    }
    setCategoryId = (categoryId) => {
        this.setState({categoryId: categoryId});
    }

    setLocationId = (locationId) => {
        this.setState({locationId: locationId});
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
                    error={this.state.errorTitle}
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
                />
                <p>Duration(hh:mm)</p>
                <TextField
                    id="time"
                    name={"Duration(hh:mm)"}
                    type="time"
                    defaultValue="01:00"
                    onChange={this.handleDuration}
                    InputLabelProps={{
                        shrink: true,
                    }}
                    inputProps={{
                        step: 900, // 15 min
                    }}
                />
                <Category
                    value={this.state.categoryId}
                    setCategoryId={this.setCategoryId}
                />
                <Location
                    value={this.state.locationId}
                    setLocationId={this.setLocationId}
                />
                <div>Start Date</div>
                <MuiPickersUtilsProvider utils={DateFnsUtils}>
                    <DatePicker
                        minDate={this.state.startDate}
                        margin="normal"
                        label="Date picker"
                        value={this.state.startDate}
                        // onChange={this.handleDateChange}
                    />
                    <TimePicker
                        margin="normal"
                        label="Time picker"
                        value={this.state.startDate}
                        // onChange={this.handleDateChange}
                    />
                </MuiPickersUtilsProvider>
                <Select title={'Access'}
                        name={'accessType'}
                        options={this.state.accessOptions}
                        value={this.state.accessType}
                        placeholder={'Enter access type of event'}
                        handleChange={this.handleInput}
                />
                <TextArea
                    title={"Description "}
                    rows={10}
                    value={this.state.description}
                    name={"description"}
                    handleChange={this.handleTextArea}
                    placeholder={"Describe details about event"}
                    error={this.state.errorDescription}
                />


                {this.state.formQueryStatus === 0 && (<div>Status: Sending request...</div>)}
                {this.state.formQueryStatus === 1 && (<div>Status: Event created successfully</div>)}
                {this.state.formQueryStatus === 2 && (<div>Status: Something went wrong.....</div>)}
                <Button
                    action={this.handleFormSubmit}
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





