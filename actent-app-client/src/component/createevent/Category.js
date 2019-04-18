import React from "react";
import ReactDOM from "react-dom";
import axios from 'axios';


class Category extends React.Component {
    state = {
        parentCategory: undefined,
        childCategory: undefined,
        parentCategories: [],
        childCategories: [],
        id: 0,
        name: undefined
    };

    handleChangeParentCategories = (event) => {
        const v = event.target.value === 'Select category of event' ? undefined : event.target.value;
        if (v !== undefined && v !== '') {
            this.setState({parentCategory: v}, () => this.getChildCategories());
        } else {
            this.setState({
                parentCategory: undefined,
                childCategory: undefined,
                childCategories: []
            });
        }
        this.props.setCategoryId(undefined);
    };

    handleChangeChildCategories = (event) => {
        const v = event.target.value === 'Select subcategory of event' ? undefined : event.target.value;

        this.setState({childCategory: v});
        this.props.setCategoryId(v);
    };

    componentDidMount() {
        this.getParentCategories()
    };

    getParentCategories = () => {
        axios.get(`http://localhost:8080/api/v1/categories/parentsubcategories`)
            .then(res => {
                const categories = res.data;
                this.setState({
                    parentCategories: categories,
                });
                console.log(categories);
            })
    };

    getChildCategories = () => {
        // console.log(`getChildCategories: ${this.state.parentCategory}`);
        axios.get(`http://localhost:8080/api/v1/categories/subcategories/${this.state.parentCategory}`)
            .then(res => {
                // console.log(res.data);
                const categories = res.data;
                this.setState({
                    childCategories: categories,
                });
                if (categories.length > 0) {
                    this.props.setCategoryId(undefined);
                } else {
                    this.props.setCategoryId(this.state.parentCategory);
                }
                // this.props.setCategoryId(categories.length ? undefined : this.state.parentCategory);
            })
    };

    render() {
        return (
            <div className="form-group">
                <label>Category</label>
                <div className="selectStyle">
                    <select className="browser-default custom-select" onChange={this.handleChangeParentCategories}
                            value={this.state.parentCategory}>
                        <option selected="selected">Select category of event</option>
                        {/*<option key="None" value="None"></option>*/}
                        {this.state.parentCategories.map(a => {
                                return (
                                    <option key={a.id} value={a.id}>{a.name}</option>
                                )
                            }
                        )
                        }
                    </select>
                </div>
                <span>{!this.state.childCategories.length && this.props.errorMessage}</span>
                {this.state.parentCategory && (this.state.childCategories.length > 0) && (<div>
                        <label>Subcategory</label>
                        <select className="browser-default custom-select" onChange={this.handleChangeChildCategories}
                                value={this.state.childCategory}>
                            <option selected="selected">Select subcategory of event</option>
                            {/*<option key="None" value="None"></option>*/}
                            {this.state.childCategories.map(a => {
                                    return (
                                        <option key={a.id} value={a.id}>{a.name}</option>
                                    )
                                }
                            )
                            }
                        </select>
                        <span>{this.props.errorMessage}</span>
                    </div>
                )}
            </div>
        );
    }
}

export default Category;