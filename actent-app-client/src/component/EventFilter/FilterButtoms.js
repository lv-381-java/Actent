import React, { Component } from 'react';
import { MDBBtn, MDBCollapse } from 'mdbreact';
import DatePicker from './DatePick';
import CityInput from './CityInput';
import CategoryList from './CategoryList';

export default class FilterButtoms extends Component {
    state = {
        collapseID: '',
        filteredEvents: [],
        dateButtonColor: 'info',
        cityButtonColor: 'info',
        categoryButtonColor: 'info',
    };

    toggleCollapse = collapseID => () => {
        this.setState(prevState => ({
            collapseID: prevState.collapseID !== collapseID ? collapseID : '',
        }));
    };

    setFilteredEvents = () => {
        console.log('in clearButtons');
        console.log(this.props.filteredEvents);
        this.setState({ filteredEvents: this.props.filteredEvents }, () => console.log(this.state.filteredEvents));
        console.log('in clearButtons end');
    };

    setCategoryButtonColor = color => {
        this.setState({ categoryButtonColor: color });
    };
    setCityButtonColor = color => {
        this.setState({ cityButtonColor: color });
    };
    setDateButtonColor = color => {
        this.setState({ dateButtonColor: color });
    };

    render() {
        console.log('this.props.filteredEvents.length');
        console.log(this.state.filteredEvents.length);
        const button =
            this.props.filteredEvents.length > 0 ? (
                <MDBBtn color='deep-orange' onClick={this.props.cleanFilter} style={{ marginBottom: '1rem' }}>
                    Clean Filter
                </MDBBtn>
            ) : null;
        return (
            <>
                <MDBBtn
                    color={this.state.categoryButtonColor}
                    onClick={this.toggleCollapse('category')}
                    style={{ marginBottom: '1rem' }}>
                    Add category filter
                </MDBBtn>
                <MDBBtn
                    color={this.state.dateButtonColor}
                    onClick={this.toggleCollapse('date')}
                    style={{ marginBottom: '1rem' }}>
                    Add data filter
                </MDBBtn>
                <MDBBtn
                    color={this.state.cityButtonColor}
                    onClick={this.toggleCollapse('city')}
                    style={{ marginBottom: '1rem' }}>
                    Select City
                </MDBBtn>

                <MDBCollapse id='category' isOpen={this.state.collapseID}>
                    <div className='container'>
                        <CategoryList
                            categoriesId={this.props.categoriesId}
                            setCategoriesId={this.props.setCategoriesId}
                            setButtonColor={this.setCategoryButtonColor}
                            categories={this.props.categories}
                        />
                    </div>
                </MDBCollapse>
                <MDBCollapse id='date' isOpen={this.state.collapseID}>
                    <div className='container'>
                        <DatePicker setButtonColor={this.setDateButtonColor} setDateRange={this.props.setDateRange} />
                    </div>
                </MDBCollapse>
                <MDBCollapse id='city' isOpen={this.state.collapseID}>
                    <div className='container'>
                        <CityInput
                            setButtonColor={this.setCityButtonColor}
                            cityName={this.props.cityName}
                            setCity={this.props.setCity}
                        />
                    </div>
                </MDBCollapse>
            </>
        );
    }
}
