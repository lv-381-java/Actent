import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';

const styles = theme => ({
  button: {
    display: 'block',
    marginTop: theme.spacing.unit * 2,
  },
  formControl: {
    margin: theme.spacing.unit,
    minWidth: 120,
  },
});

class UserTypeFilter extends React.Component {
  state = {
    userType: '',
    open: false,
  };

  handleChange = event => {
    this.props.setUserType(event.target.value);
    this.setState({ [event.target.name]: event.target.value });
  };

  handleClose = () => {
    this.setState({ open: false });
  };

  handleOpen = () => {
    this.setState({ open: true });
  };

  render() {
    const { classes } = this.props;

    return (
      <form autoComplete='off'>
        <FormControl className={classes.formControl}>
          <Select
            open={this.state.open}
            onClose={this.handleClose}
            onOpen={this.handleOpen}
            value={this.state.userType}
            onChange={this.handleChange}
            inputProps={{
              name: 'userType',
            }}>
            <MenuItem value=''>
              <em>None</em>
            </MenuItem>
            <MenuItem value={'SPECTATOR'}>Spectator</MenuItem>

            <MenuItem value={'PARTICIPANT'}>Participant</MenuItem>
          </Select>
        </FormControl>
      </form>
    );
  }
}

UserTypeFilter.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(UserTypeFilter);
