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
        name: undefined,
    };

    handleChangeParentCategories = (event) => {
        if (event.target.value === "None") {
            this.setState({
                parentCategory: undefined,
                childCategory: undefined
            });
        } else {
            this.setState({parentCategory: event.target.value}, () => this.getChildCategories());
        }
    }
    handleChangeChildCategories = (event) => {
        if (event.target.value === "None") {
            this.setState({childCategory: undefined});
            this.props.setCategoryId(undefined)
        } else {
            this.setState({childCategory: event.target.value});
            this.props.setCategoryId(event.target.value)
        }
    }

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
        console.log(`getChildCategories: ${this.state.parentCategory}`);
        axios.get(`http://localhost:8080/api/v1/categories/subcategories/${this.state.parentCategory}`)
            .then(res => {
                console.log(res.data);
                const categories = res.data;
                this.setState({
                    childCategories: categories,
                });
            })
    };

    render() {
        return (
            <div className="form-group">
                <label>Category</label>
                <div className="selectStyle">
                    <select className="browser-default custom-select" onChange={this.handleChangeParentCategories}
                            value={this.state.parentCategory}>
                        <option key="None" value="None"></option>
                        {this.state.parentCategories.map(a => {
                                return (
                                    <option key={a.id} value={a.id}>{a.name}</option>
                                )
                            }
                        )
                        }
                    </select>
                </div>
                {this.state.parentCategory && (<div>
                        <label>Subcategory</label>
                        <select className="browser-default custom-select" onChange={this.handleChangeChildCategories}
                                value={this.state.childCategory}>
                            <option key="None" value="None"></option>
                            {this.state.childCategories.map(a => {
                                    return (
                                        <option key={a.id} value={a.id}>{a.name}</option>
                                    )
                                }
                            )
                            }
                        </select>
                    </div>
                )}
            </div>
        );
    }
}

export default Category;