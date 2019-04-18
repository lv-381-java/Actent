import React from 'react';
import Dialog from '@material-ui/core/Dialog';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import Delete from './DeleteMessage';
import Edit from './EditMessage';

export default class Confirm extends React.Component{

    state = {
        messageId: undefined,
        senderId: undefined,
        open: false,
        messageText: undefined,
        chatId: undefined
    };

    constructor(props){
        super(props);
        this.state.open = props.open;
        this.state.messageId = props.messageId;
        this.state.senderId = props.senderId;
        this.state.messageText = document.getElementById(`textField_${props.messageId}`) !== null ?
            document.getElementById(`textField_${props.messageId}`).textContent : '';
        this.state.chatId = props.chatId;
    }

    dialogClose = () => {
        this.setState({open: false});
    };

    render() {
        return(
            <Dialog
                open={this.state.open}
                onClose={this.dialogClose}
            >
                <DialogTitle id="customized-dialog-title">
                    Take your choice
                </DialogTitle>

                <DialogContent className='buttonsActions'>
                    {this.state.messageText !== '' ? <Edit
                                                           messageId={this.state.messageId}
                                                           senderId={this.state.senderId}
                                                           messageText={this.state.messageText}
                                                           chatId={this.state.chatId}
                                                      />
                                                   : null
                    }
                    <Delete messageId={this.state.messageId} senderId={this.state.senderId}/>
                </DialogContent>
            </Dialog>
        );
    }
}