import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import FormControl from '@material-ui/core/FormControl';
import MenuItem from '@material-ui/core/MenuItem';
import Select from '@material-ui/core/Select';
import axios from 'axios';

const styles = theme => ({
  button: {
    display: 'block',
    marginTop: theme.spacing.unit * 2,
  },
  formControl: {
    margin: theme.spacing.unit,
    minWidth: 150,
  },
});

class CategoryFilter extends React.Component {
  state = {
    category: '',
    categories: [],
    open: false,
  };

  componentDidMount() {
    this.getCategories();
  }

  handleChange = event => {
    this.props.setCategory(event.target.value);
    this.setState({ [event.target.name]: event.target.value });
  };

  handleClose = () => {
    this.setState({ open: false });
  };

  handleOpen = () => {
    this.setState({ open: true });
  };

  getCategories() {
    axios
      .get('/categories')
      .then(response => {
        const categories = response.data;
        this.setState({ categories: categories });
      })
      .catch(function(error) {
        console.log(error);
      });
  }

  render() {
    const { classes } = this.props;

    return (
      <form autoComplete='off'>
        <FormControl className={classes.formControl}>
          <Select
            open={this.state.open}
            onClose={this.handleClose}
            onOpen={this.handleOpen}
            value={this.state.category}
            onChange={this.handleChange}
            inputProps={{
              name: 'category',
            }}>
            <MenuItem value=''>
              <em>None</em>
            </MenuItem>
            {this.state.categories.map(category => (
              <MenuItem key={category.id.toString()} value={category.name.toString()}>
                {category.name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      </form>
    );
  }
}

CategoryFilter.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(CategoryFilter);
