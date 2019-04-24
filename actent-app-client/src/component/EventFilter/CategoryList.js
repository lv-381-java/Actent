import React from 'react';
import Checkbox from '@material-ui/core/Checkbox';
import FormGroup from '@material-ui/core/FormGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';

export default class CategoryList extends React.Component {
    state = {
        categories: this.props.categories,
        checked: undefined,
    };

    toggleChange = event => {
        if (event.target.checked === true) {
            this.props.addFilterCategorieId(event.target.id);
        } else {
            this.props.deleteFilterCategorieId(event.target.id);
        }
    };

    render() {
        return (
            <div className='row'>
                {this.props.categories.map(category => {
                    return (
                        <div key={category.id} className='col-md-4 col-sm-12 align-self-center cart'>
                            <FormGroup row key={category.id}>
                                <FormControlLabel
                                    control={
                                        <Checkbox
                                            id={category.id.toString()}
                                            color='primary'
                                            onChange={this.toggleChange}
                                            onClick={this.props.setButtonColor}
                                        />
                                    }
                                    label={category.name}
                                />
                            </FormGroup>
                        </div>
                    );
                })}
            </div>
        );
    }
}
