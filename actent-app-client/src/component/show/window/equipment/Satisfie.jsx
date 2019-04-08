import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent';
import DialogActions from '@material-ui/core/DialogActions';
import Dialog from '@material-ui/core/Dialog';
import RadioGroup from '@material-ui/core/RadioGroup';
import Radio from '@material-ui/core/Radio';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import { getCurrentUser } from '../../../../util/apiUtils';

const options = [
    'Satisfied',
    'Unsatified',
];

class ConfirmationDialogRaw extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            value: props.value,
        };
    }

    // TODO
    componentWillReceiveProps(nextProps) {
        if (nextProps.value !== this.props.value) {
            this.setState({ value: nextProps.value });
        }
    }

    handleEntering = () => {
        this.radioGroupRef.focus();
    };

    handleCancel = () => {
        this.props.onClose(this.props.value);
    };

    handleOk = () => {
        this.props.handleNewOkValue(this.state.value);
        this.props.onClose(this.state.value);
    };

    handleChange = (event, value) => {
        this.setState({ value });
    };

    render() {
        const { value, handleNewOkValue, ...other } = this.props;

        return (
            <Dialog
                disableBackdropClick
                disableEscapeKeyDown
                maxWidth="xs"
                onEntering={this.handleEntering}
                aria-labelledby="confirmation-dialog-title"
                {...other}
            >
                <DialogTitle id="confirmation-dialog-title">Is satisfy</DialogTitle>

                <DialogContent>
                    <RadioGroup
                        ref={ref => {
                            this.radioGroupRef = ref;
                        }}

                        value={this.state.value}
                        onChange={this.handleChange}
                    >
                        {options.map(option => (
                            <FormControlLabel value={option} key={option} control={<Radio />} label={option} />
                        ))}
                    </RadioGroup>
                </DialogContent>

                <DialogActions>
                    <Button onClick={this.handleCancel} color="primary">
                        Cancel
                    </Button>
                    <Button onClick={this.handleOk} color="primary">
                        Ok
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}

ConfirmationDialogRaw.propTypes = {
    onClose: PropTypes.func,
    value: PropTypes.string,
};

const styles = theme => ({
    root: {
        width: '100%',
        maxWidth: 360,
        backgroundColor: theme.palette.background.paper,
    },
    paper: {
        width: '80%',
        maxHeight: 435,
    },
});

class ConfirmationDialog extends React.Component {

    state = {
        open: false,
        value: this.props.currentEquipment.satisfied ? options[0] : options[1],
        currentUserId: undefined,
    };

    constructor(props) {
        super(props);
        this.setCurrentUserId();
    }

    handleClickListItem = () => {
        this.setState({ open: true });
    };

    handleClose = value => {
        this.setState({ value, open: false });
    };

    handleNewOkValue = (newValue) => {
        const isSatisfied = (newValue === options[0]) ? true : false;

        let equipment = {
            assignedEventId: this.props.currentEquipment.assignedEventId,
            assignedUserId: this.props.currentEquipment.assignedUserId,
            description: this.props.currentEquipment.description,
            satisfied: isSatisfied,
            title: this.props.currentEquipment.title
        }

        this.props.handleUpdateEquipment(this.props.currentEquipment.id, equipment);
    }

    setCurrentUserId = () => {

        getCurrentUser().then(res => this.setState({ currentUserId: res.data.id })).catch(e => console.error(e));
    }

    render() {
        const { classes } = this.props;

        const isDisabled = !(this.props.currentEquipment.assignedUserId === this.state.currentUserId);

        return (
            <div className={classes.root}>
                <List>
                    <ListItem
                        button
                        onClick={this.handleClickListItem}
                        disabled={isDisabled}
                    >
                        <ListItemText primary="Is satisfy" secondary={this.state.value} />
                    </ListItem>

                    <ConfirmationDialogRaw
                        classes={{
                            paper: classes.paper,
                        }}
                        open={this.state.open}
                        onClose={this.handleClose}
                        value={this.state.value}
                        handleNewOkValue={this.handleNewOkValue}
                    />
                </List>
            </div>
        );
    }
}

ConfirmationDialog.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(ConfirmationDialog);