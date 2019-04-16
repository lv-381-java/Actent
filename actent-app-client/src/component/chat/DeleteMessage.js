import React from 'react';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import axios from "axios";
import {NotificationContainer, NotificationManager} from "react-notifications";
import { deleteURL } from '../websockets/ws';

export default class Delete extends React.Component{

    state = {
        messageId: undefined,
        senderId: undefined,
        open: undefined
    };

    constructor(props){
        super(props);
        this.state.messageId = props.messageId;
        this.state.senderId = props.senderId;
        this.state.open = false;
    }

    //TODO: hide the main dialog after deleting message
    deleteMessage = () => {
        deleteURL(this.state.messageId, this.state.senderId);
            // .then(response => {
            NotificationManager.info('Your message is successfully deleted', 'Deleted', 5000);
            // let messageArea = document.getElementById('messageArea');
            // let message = document.getElementById(`msg_${this.state.messageId}sender_${this.state.senderId}`);
            // messageArea.removeChild(message);
        // }).catch(() => {
        //     NotificationManager.error('Something went wrong!', 'Error!', 5000);
        // });
        this.dialogClose();
    };

    dialogClose = () => {
        this.setState({open: false});
    };

    dialogOpen = () => {
        this.setState({open: true});
    };

    render() {
        return(
            <div>
                <Button variant="contained" color="primary" onClick={this.dialogOpen}>
                    Delete
                </Button>
                <Dialog
                    open={this.state.open}
                    onClose={this.dialogClose}
                >
                    <DialogTitle id="customized-dialog-title">
                        You sure?
                    </DialogTitle>

                    <DialogContent className='buttonsActions'>
                        <Button onClick={this.dialogClose} color="primary">
                            No
                        </Button>
                        <Button onClick={this.deleteMessage} color="primary">
                            Yes
                        </Button>
                    </DialogContent>
                </Dialog>

                <NotificationContainer />

            </div>

        );
    }
}