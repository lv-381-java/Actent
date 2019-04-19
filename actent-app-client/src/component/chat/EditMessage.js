import React from 'react';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import {NotificationContainer, NotificationManager} from "react-notifications";
import DialogActions from "@material-ui/core/DialogActions";
import TextField from "@material-ui/core/TextField";
import {updateURL} from '../websockets/ws';

export default class EditMessage extends React.Component{

    state = {
        messageId: undefined,
        senderId: undefined,
        open: undefined,
        messageText: undefined,
        editMessage: undefined,
        chatId: undefined
    };

    constructor(props){
        super(props);
        this.state.messageId = props.messageId;
        this.state.senderId = props.senderId;
        this.state.open = false;
        this.state.messageText = props.messageText;
        this.state.chatId = props.chatId;
        this.state.editMessage = props.messageText;
    }

    dialogClose = () => {
        this.setState({open: false});
    };

    dialogOpen = () => {
        this.setState({open: true});
    };

    handleMessage = (event) => {
        this.setState({editMessage: event.target.value});
    };

    editMessage = () => {
        if(this.state.messageText === this.state.editMessage){
            NotificationManager.info('That was the same message (((', 'OK', 5000);
            this.dialogClose();
        }else{

            updateURL(this.state.chatId, this.state.editMessage, this.state.senderId, this.state.messageId);

            NotificationManager.info('Your message is successfully edited', 'Edited', 5000);
            this.dialogClose();
        }
    };

    render() {
        return(
            <div>
                <Button variant="contained" color="primary" onClick={this.dialogOpen}>
                    Edit message
                </Button>
                <Dialog
                    open={this.state.open}
                    onClose={this.dialogClose}
                >
                    <DialogTitle id="customized-dialog-title">
                        Re-write your message
                    </DialogTitle>
                    <DialogContent>
                        <TextField
                            id="outlined-email-input"
                            label="Edit message"
                            className="FormField__Input"
                            margin="normal"
                            defaultValue={this.state.messageText}
                            variant="outlined"
                            onChange={this.handleMessage}
                        />
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={this.dialogClose} color="primary">
                            Disagree
                        </Button>
                        <Button onClick={this.editMessage} color="primary" autoFocus>
                            Agree
                        </Button>
                    </DialogActions>
                </Dialog>

                <NotificationContainer />

            </div>

        );
    }
}