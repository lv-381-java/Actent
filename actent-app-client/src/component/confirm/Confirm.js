import React from 'react';
import queryString from 'query-string'
import axios from 'axios';
import {API_BASE_URL} from "../../constants/apiConstants";

export default class Confirm extends React.Component {

    componentDidMount() {
        const values = queryString.parse(this.props.location.search);

        const params = {
            login: values.login,
            uuid: values.uuid
        };

        axios.post(API_BASE_URL + '/confirm', params).then(res => {
            console.log(res.data);
            if(res.data.verification === "VERIFIED"){
                this.props.history.push('/');
            }
        });
    }

    render() {
        return(
            <div></div>
        );
    }

}

