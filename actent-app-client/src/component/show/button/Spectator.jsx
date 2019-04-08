import React from 'react';
import Button from '@material-ui/core/Button/index';
import Dialog from '@material-ui/core/Dialog/index';
import DialogActions from '@material-ui/core/DialogActions/index';
import DialogTitle from '@material-ui/core/DialogTitle/index';
import Axios from 'axios';

class Spectator extends React.Component {

    state = {
        open: false,
        assigne: false,
    };

    assigneAsParticipant = () => {

        let userId = this.props.currentUserId
        let eventId = this.props.eventId
        let eventUser = {userId, eventId, eventUserType: 'SPECTATOR'}
        
        Axios.post(`http://localhost:8080/api/v1/eventsUsers`, eventUser).then(eve => {
            this.setState({
                ...this.state,
                id: eve.data.id,
                assigne: true,
            });
        }).catch(error => {
            this.setState({
                ...this.state,
                assigne: true,
            });
            console.log(error);
        });
    };

    unassigneUser = () => {
        Axios.delete(`http://localhost:8080/api/v1/eventsUsers/${this.state.id}`).then(eve => {
            this.setState({
                ...this.state,
                assigne: false,
            })
        }
        ).catch(error => {
            this.setState({
                ...this.state,
                assigne: false,
            });
            console.log(error);
        });
    }

    handleClickOpen = () => {
        this.setState({ ...this.state, open: true });
    };

    handleClose = () => {
        this.setState({ ...this.state, open: false });
    };

    isPresent = () => {

    }

    handleUnAssignedUser = () => {
        this.unassigneUser();
        this.handleClose();
    }

    handleAssignedUserId = () => {
        this.assigneAsParticipant();
        this.handleClose();
    };

    handleAgreeDiscardAssigne = () => {
        this.handleClose();
    };

    render() {
        let assigneButton;
    
        if (this.props.currentUserId) {

            if (this.state.assigne) {

                assigneButton = (
                        <div>
                            <Button variant="outlined" color="primary" onClick={this.handleClickOpen}>
                                Unassigne as spectator
                            </Button>

                            <Dialog
                                    open={this.state.open}
                                    onClose={this.handleClose}
                                    aria-labelledby="alert-dialog-title"
                                    aria-describedby="alert-dialog-description"
                            >
                                <DialogTitle id="alert-dialog-title">{"Are you sure?"}</DialogTitle>
                                <DialogActions>
                                    <Button onClick={this.handleClose} color="primary">
                                        Cancel
                                    </Button>
                                    <Button onClick={this.handleUnAssignedUser} color="primary" autoFocus>
                                        Agree
                                    </Button>
                                </DialogActions>
                            </Dialog>
                        </div>
                );
            } else {

                    assigneButton = (
                        <div>
                            <Button variant="outlined" color="primary" onClick={this.handleClickOpen}>
                                Assigne as spectator
                            </Button>

                            <Dialog
                                    open={this.state.open}
                                    onClose={this.handleClose}
                                    aria-labelledby="alert-dialog-title"
                                    aria-describedby="alert-dialog-description"
                            >
                                <DialogTitle id="alert-dialog-title">{"Are you sure?"}</DialogTitle>
                                <DialogActions>
                                    <Button onClick={this.handleClose} color="primary">
                                        Cancel
                                    </Button>
                                    <Button onClick={this.handleAssignedUserId} color="primary" autoFocus>
                                        Agree
                                    </Button>
                                </DialogActions>
                            </Dialog>
                        </div>
                );
            }
        } else {
            assigneButton = (
                    <div>
                        <Button variant="outlined" color="primary" onClick={this.handleClickOpen}>
                            Discard assigne
                        </Button>

                        <Dialog
                                open={this.state.open}
                                onClose={this.handleClose}
                                aria-labelledby="alert-dialog-title"
                                aria-describedby="alert-dialog-description"
                        >
                            <DialogTitle id="alert-dialog-title">{"Are you sure?"}</DialogTitle>

                            <DialogActions>
                                <Button onClick={this.handleClose} color="primary">
                                    Cancel
                                </Button>
                                <Button onClick={this.handleAgreeDiscardAssigne} color="primary" autoFocus>
                                    Agree
                                </Button>
                            </DialogActions>
                        </Dialog>
                    </div>
            )
        }
        return (
                <div>
                    <div>
                        {assigneButton}
                    </div>
                </div>
        );
    }
}

export default Spectator;
