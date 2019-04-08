import React from 'react';
import Checkbox from '@material-ui/core/Checkbox';
import FormGroup from '@material-ui/core/FormGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';

export default class CategoryList extends React.Component {
    state = {
        categories: this.props.categoriesId,
        color: 'info',
    };

    addElemntIntoArr(id) {
        let intId = parseInt(id);
        let newCategories = [];
        newCategories.push(intId);
        this.setState({ categories: this.state.categories.concat(newCategories) }, () => this.changeButtonCollor());
    }

    deleteElementFromArr(id) {
        let intId = parseInt(id);
        let filteredArr = this.state.categories.filter(e => e !== intId);
        this.setState({ categories: filteredArr }, () => this.changeButtonCollor());
    }

    handleCheck = () => {
        this.setState({ checked: !this.state.checked });
    };

    changeButtonCollor = () => {
        let categories = this.state.categories;
        this.props.setCategoriesId(categories);
        categories === undefined || categories.length == 0
            ? this.props.setButtonColor('info')
            : this.props.setButtonColor('success');
    };

    toggleChange = event => {
        console.log(event.target.checked);
        console.log(event.target.id);
        if (event.target.checked === true) {
            this.addElemntIntoArr(event.target.id);
        }

        if (event.target.checked === false) {
            console.log('false');
            this.deleteElementFromArr(event.target.id);
        }

        console.log(this.state.categories);
    };

    render() {
        return (
            <div className='row'>
                {console.log('this.props.categoriesId')}
                {console.log(this.state.categories)}
                {console.log(this.props.categoriesId)}
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
