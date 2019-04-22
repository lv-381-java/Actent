import React from 'react';
import Button from '@material-ui/core/Button/index';
import Dialog from '@material-ui/core/Dialog/index';
import DialogActions from '@material-ui/core/DialogActions/index';
import DialogTitle from '@material-ui/core/DialogTitle/index';
import Axios from 'axios';

class Participant extends React.Component {

    state = {
        open: false,
    };

    assigneAsParticipant = () => {

        let userId = this.props.currentUserId
        let eventId = this.props.eventId
        let eventUser = {userId, eventId, eventUserType: 'PARTICIPANT'}
        
        Axios.post(`http://localhost:8080/api/v1/eventsUsers`, eventUser).then(eve => {
            this.props.setAssigneIdInButton(eve.data.id);
        }).catch(error => {
            console.log(error);
        });
    };

    unassigneUser = () => {
        Axios.delete(`http://localhost:8080/api/v1/eventsUsers/${this.props.assID}`).then(eve => {}
        ).catch(error => {
            console.log(error);
        });
    }

    handleClickOpen = () => {
        this.setState({ ...this.state, open: true });
    };

    handleClose = () => {
        this.setState({ ...this.state, open: false });
    };

    handleUnAssignedUser = () => {
        this.props.setValue(false);
        this.unassigneUser();
        this.handleClose();
    }

    handleAssignedUserId = () => {
        this.props.setValue(true);
        this.assigneAsParticipant();
        this.handleClose();
    };

    handleAgreeDiscardAssigne = () => {
        this.handleClose();
    };

    render() {
        let assigneButton;
    
        if (this.props.currentUserId) {

            if (this.props.assigne) {

                assigneButton = (
                        <div>
                            <Button className='btn btn-primary' variant="outlined" color="primary" onClick={this.handleClickOpen}>
                                Unassigne as participiant
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
                            <Button className='btn btn-primary' variant="outlined" color="primary" onClick={this.handleClickOpen}>
                                Assigne as participiant
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
                        <Button className='btn btn-primary' variant="outlined" color="primary" onClick={this.handleClickOpen}>
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

export default Participant;
