import React from 'react';
import Button from '@material-ui/core/Button/index';
import Dialog from '@material-ui/core/Dialog/index';
import DialogActions from '@material-ui/core/DialogActions/index';
import DialogTitle from '@material-ui/core/DialogTitle/index';
import DialogContent from "@material-ui/core/DialogContent";
import Axios from 'axios';

function
formatDate(n) {
    if (n < 9) return "0" + n;
    return n;
};

class Participant extends React.Component {

    state = {
        open: false,
        eventsUsers: [],
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
        Axios.delete(`http://localhost:8080/api/v1/eventsUsers/${this.props.assID}`)
            .then(eve => {
                }
            ).catch(error => {
            console.log(error);
        });
    };

    getCurrentUserAssignedEvents = () => {
        let userId = this.props.currentUserId;
        let databaseStartDate = new Date(this.props.startDate);

        let formattedStartDate = new Date(databaseStartDate.getTime() - (3 * 1000 * 60 * 60));

        let startDate = formattedStartDate.getFullYear() + "-" +
            formatDate(parseInt(formattedStartDate.getMonth() + 1)) + "-" +
            formatDate(formattedStartDate.getDate()) + " " +
            formatDate(formattedStartDate.getHours()) + ":" +
            formatDate(formattedStartDate.getMinutes()) + ":" +
            formatDate(formattedStartDate.getSeconds());

        let databaseEndDate = new Date(formattedStartDate.getTime() + this.props.duration);
        let endDate = databaseEndDate.getFullYear() + "-" +
            formatDate(parseInt(databaseEndDate.getMonth() + 1)) + "-" +
            formatDate(databaseEndDate.getDate()) + " " +
            formatDate(databaseEndDate.getHours()) + ":" +
            formatDate(databaseEndDate.getMinutes()) + ":" +
            formatDate(databaseEndDate.getSeconds());

        let url = `/eventsUsers/assignedEvents/${userId}/${startDate}/${endDate}`;
        console.log(url);
        Axios
            .get(url)
            .then(response => {
                const eventsUsers = response.data;
                this.setState({eventsUsers: eventsUsers, open: true});
            })
            .catch(function (error) {
                console.error(error);
            });
    };

    handleClickOpen = () => {
        this.getCurrentUserAssignedEvents();
    };

    handleClose = () => {
        this.setState({...this.state, open: false});
    };

    handleUnAssignedUser = () => {
        this.props.setValue(false);
        this.unassigneUser();
        this.handleClose();
    };

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
                    <phatom>
                        <Button className='btn btn-primary' variant="outlined" color="primary"
                                onClick={this.handleClickOpen}>
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
                    </phatom>
                );
            } else {

                assigneButton = (
                    <phatom>
                        <Button className='btn btn-primary' variant="outlined" color="primary"
                                onClick={this.handleClickOpen}>
                            Assigne as participiant
                        </Button>

                        <Dialog
                            open={this.state.open}
                            onClose={this.handleClose}
                            aria-labelledby="alert-dialog-title"
                            aria-describedby="alert-dialog-description"
                        >
                            <DialogTitle id="alert-dialog-title">{"Do you want to assign?"}</DialogTitle>
                            {this.state.eventsUsers.length > 0 && (
                                <DialogContent>
                                    <p>You already have event in this time:</p>
                                    {this.state.eventsUsers.map(event => {
                                        return (
                                            <li className='text-center' key={event.eventId.toString()}>
                                                <a rel="noopener"
                                                   target="_blank"
                                                   href={`/show/${event.eventId}`}>{event.eventTitle}  </a>
                                            </li>)
                                    })}
                                </DialogContent>)}
                            <DialogActions>
                                <Button onClick={this.handleClose} color="primary">
                                    Cancel
                                </Button>
                                <Button onClick={this.handleAssignedUserId} color="primary" autoFocus>
                                    Agree
                                </Button>
                            </DialogActions>
                        </Dialog>
                    </phatom>
                );
            }
        } else {
            assigneButton = (
                <phatom>
                    <Button className='btn btn-primary' variant="outlined" color="primary"
                            onClick={this.handleClickOpen}>
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
                    </phatom>
            )
        }
        return (
            <phatom>
                {assigneButton}
            </phatom>
                    

        );
    }
}

export default Participant;
