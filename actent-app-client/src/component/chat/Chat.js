import React from 'react';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import {connect, sendMessage, showMessageOutput, sendImage, showMessageOutputPrepend} from '../websockets/ws';
import '../../styles/chat.css';
import axios from 'axios';
import {getCurrentUser} from '../../util/apiUtils';
import {
    API_BASE_URL, API_CHAT_COUNT_URL, API_CHAT_URL,
    API_CURRENT_MESSAGES,
    API_PAGE_SIZE_20
} from "../../constants/apiConstants";
import DownloadImage from './DownloadImage';
import { apiUrl } from "../profile/Profile";
import { NotificationContainer } from "react-notifications";

let bool = true;

export default class Chat extends React.Component {

    state = {
        chatId: undefined,
        messages: [],
        currentUserId: undefined,
        message: '',
        imageName: '',
        imageData: {},
        imageId: undefined,
        counterPages: undefined,
        countMessages: undefined
    };

    constructor(props) {
        super(props);
        this.state.chatId = props.chatId;
        this.setCurrentUserId();
    }

    setCurrentUserId = () => {
        getCurrentUser().then(
            res => {
                this.setState({currentUserId: res.data.id})
            }).catch(e => {
            console.log(e)
        });
    };

    getPageableMessages = () => {

        axios.get(API_BASE_URL + API_CURRENT_MESSAGES + `/${this.state.chatId}/${this.state.counterPages}/${API_PAGE_SIZE_20}`)
            .then(res => {
                this.setState({messages: res.data.concat(this.state.messages)}, () => {
                    if (bool) {
                        res.data.forEach(msg => showMessageOutput(msg));
                        bool = false;
                    } else {
                        res.data.reverse().forEach(msg => showMessageOutputPrepend(msg));
                    }
                })
            });
        this.setState({counterPages: this.state.counterPages - 1});
    };

    getCountOfMessages = () => {
        axios.get(API_BASE_URL + API_CHAT_URL + `/${this.state.chatId}` + API_CHAT_COUNT_URL).then(res => {
            this.setState({countMessages: res.data.countOfMessages}, () => {
                let pages = Math.ceil(this.state.countMessages / 20) - 1;
                this.setState({counterPages: pages}, () => {
                    this.getPageableMessages();
                });
            })
        })
    };

    componentWillMount() {
        this.getCountOfMessages();
    }

    componentDidMount() {
        this.handleConnect();
    };

    handleSendImage = () => {
        sendImage(this.state.chatId, this.state.currentUserId, this.state.imageData.filePath);
        this.setState({
            imageName: '',
            imageData: {},
            imageId: undefined
        })
    };

    handleSendMessage = () => {
        sendMessage(this.state.chatId, this.state.currentUserId, this.state.message);
        this.setState({message: ''});
    };

    handleConnect = () => {
        connect(this.state.chatId);
    };

    handleInputValue = event => {
        this.setState({message: event.target.value.trim()});
    };

    saveImage = () => {
        const uploadUrl = apiUrl + '/storage/uploadFile/';
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
                this.handleSendImage();
            })
            .catch(error => {
                console.log(error);
            });
    };

    fetchData = (imageData, imageName) => {
        this.setState({
            imageData: imageData,
            imageName: imageName
        }, () => {
            this.saveImage()
        })
    };

    //TODO: add this function with its logic into main class (Chat.js) and find the way when delete method has done -> message array - 1 and re-render <li>!!!!!!!!!!!!
    //remove message from state
    delete = (messageId) => {
        this.state.messages.forEach(msg => {
            if(msg.id === messageId){
                let messagesArray = this.state.messages;
                messagesArray = messagesArray.splice(messagesArray.indexOf(msg), 1);
                this.setState({messages: messagesArray });
            }
        })
    };

    render() {

        const sendButton =
            this.state.message.length !== 0 ? (
                <Button className="FormField__Button send" variant="contained" color="primary"
                        onClick={this.handleSendMessage}>
                    Send message
                </Button>
            ) : null;

        const nextButton =
            this.state.counterPages >= 0 ? (
                <Button color="primary" className='show-button-first' onClick={this.getPageableMessages}>
                    Next messages
                </Button>
            ) : null;

        return (

            <div id="chat-page">

                <div className="chat-container">

                    {nextButton}
                    <div id="bool" />
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
                                onChange={this.handleInputValue}
                            />
                        </div>
                        {sendButton}
                        <DownloadImage fetchData={this.fetchData}/>
                    </div>
                </div>

                <NotificationContainer/>

            </div>
        );
    }

}

