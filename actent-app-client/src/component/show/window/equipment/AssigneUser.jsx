import React from 'react';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogTitle from '@material-ui/core/DialogTitle';
import { getCurrentUser } from '../../../../util/apiUtils';
import './AssignedUser.css'

class AlertDialog extends React.Component {
    state = {
        open: false,
        currentUserId: undefined,
        isDisabled: this.props.currentEquipment.assignedUserId ? true : false,
    };

    constructor(props) {
        super(props);
        this.setCurrentUserId();
    }

    handleClickOpen = () => {
        this.setState({ open: true });
    };

    handleClose = () => {
        this.setState({ open: false });
    };

    handleAssignedUserId = () => {
        let equipment = {
            assignedEventId: this.props.currentEquipment.assignedEventId,
            assignedUserId: this.state.currentUserId,
            description: this.props.currentEquipment.description,
            satisfied: this.props.currentEquipment.satisfied,
            title: this.props.currentEquipment.title
        }
        this.props.handleUpdateEquipment(this.props.currentEquipment.id, equipment);
        this.handleClose();

    };

    handleAgreeDiscardAssigne = () => {
        let equipment = {
            assignedEventId: this.props.currentEquipment.assignedEventId,
            assignedUserId: null,
            description: this.props.currentEquipment.description,
            satisfied: this.props.currentEquipment.satisfied,
            title: this.props.currentEquipment.title
        }
        
        this.props.handleUpdateEquipment(this.props.currentEquipment.id, equipment);
        this.handleClose();
    };

    setCurrentUserId = () => {

        getCurrentUser().then(res => this.setState({ currentUserId: res.data.id })).catch(e => console.error(e));
    }

    hasAccesDiscardAssigne = () => {

        console.log(this.state.currentUserId);
        console.log(this.props.currentEquipment.assignedUserId);

         console.log("has accses" + this.state.currentUserId !== this.props.currentEquipment.assignedUserId);

        if (this.state.currentUserId !== this.props.currentEquipment.assignedUserId ){
            console.log("sadadsda");
            
            this.setState({isDisabled: true})
        } else {

            this.setState({isDisabled: false})
        } 
    }

    render() {
        let assigneButton;

        let assignedMatch = !(this.props.currentEquipment.assignedUserId === this.state.currentUserId);

        if (this.props.currentEquipment.assignedUserId === null) {

            assigneButton = (
                <div>
                    <Button variant="outlined" color="primary" onClick={this.handleClickOpen} style={{ margin: "10%" }}>
                        Assigne
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
        } else{
            assigneButton = (
                <div>
                    <Button variant="outlined" color="primary" onClick={this.handleClickOpen} disabled={assignedMatch} style={{ margin: "10%" }}>
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
                {assigneButton}
                <div className="assignedUser">
                    <p>{this.props.currentEquipment.assignedUserFirstName}</p>
                    <p>{this.props.currentEquipment.assignedUserLastName}</p>
                </div>
            </div>
        );
    }
}

export default AlertDialog;