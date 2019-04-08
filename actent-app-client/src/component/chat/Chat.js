import React from 'react';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import {connect, sendMessage, showMessageOutput} from '../websockets/ws';
import '../../styles/chat.css';
import axios from 'axios';
import {API_BASE_URL, API_MESSAGES_URL} from "../../constants/apiConstants";

export default class Chat extends React.Component {

    state = {
        chatId: undefined,
        messages: []
    };

    constructor(props) {
        super(props);
        this.state.chatId = props.chatId;
    }

    getListMessages = () => {
             axios.get(API_BASE_URL + API_MESSAGES_URL + `/${this.state.chatId}`).then(res => {
                this.setState({messages: res.data}, () => {
                    this.state.messages.forEach(msg => showMessageOutput(msg))
                })
            })
    };

    componentWillMount() {
        this.getListMessages();
    }

    componentDidMount() {
        this.handleConnect();
    };

    handleSendMessage = () => {
        sendMessage(this.state.chatId);
    }

    handleConnect = () => {
        connect(this.state.chatId);
    }

    render() {

        return (

            <div id="chat-page">

                <div className="chat-container">

                    <ul id="messageArea" />

                    <div className='input'>
                        <div className="FormField">
                            <TextField
                                id="outlined-email-input"
                                label="Message"
                                className="FormField__Input"
                                type="Message"
                                name="message"
                                autoComplete=""
                                margin="normal"
                                variant="outlined"
                            />
                        </div>

                        <Button className="FormField__Button" variant="contained" color="primary" onClick={this.handleSendMessage}>
                            Send message
                        </Button>
                    </div>
                </div>

            </div>

        );
    }

}

