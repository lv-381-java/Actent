import React from 'react';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import Paper from '@material-ui/core/Paper';
import Draggable from 'react-draggable';

function PaperComponent(props) {
    return (
        <Draggable>
            <Paper {...props} />
        </Draggable>
    );
}

export default class Subscribe extends React.Component {
    state = {
        open: false,
        isSubscribe: false,
    };

    handleClickOpen = () => {
        this.setState({ open: true });
    };

    handleClose = () => {
        this.setState({ open: false });
    };

    subscribe = () => {
        console.log('subscribe');
        this.setState({ isSubscribe: true });
        this.handleClose();
        this.props.addSubscribe();
    };

    render() {
        let button = !this.state.isSubscribe ? (
            <Button variant='contained' color='primary' onClick={this.handleClickOpen}>
                Subscribe to similar events
            </Button>
        ) : null;
        return (
            <div style={{ textAlign: 'center' }}>
                {button}
                <Dialog
                    open={this.state.open}
                    onClose={this.handleClose}
                    PaperComponent={PaperComponent}
                    aria-labelledby='draggable-dialog-title'>
                    <DialogTitle id='draggable-dialog-title'>Subscribe</DialogTitle>
                    <DialogContent>
                        <DialogContentText>Do you want Subscribe to similar events ? </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={this.handleClose} color='primary'>
                            Cancel
                        </Button>
                        <Button onClick={this.subscribe} color='primary'>
                            Subscribe
                        </Button>
                    </DialogActions>
                </Dialog>
            </div>
        );
    }
}
