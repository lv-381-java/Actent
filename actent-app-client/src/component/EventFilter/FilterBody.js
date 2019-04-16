import React, { Component } from 'react';
import { MDBCard, MDBCardBody, MDBRow, MDBCol, MDBBtn, MDBCollapse } from 'mdbreact';
import DatePicker from './DatePick';
import CityInput from './CityInput';
import CategoryList from './CategoryList';

export default class FilterBody extends Component {
    state = {
        collapseID: '',
    };

    toggleCollapse = collapseID => () => {
        console.log(collapseID);
        this.props.collapseID === collapseID ? this.props.setCollapseID('') : this.props.setCollapseID(collapseID);
    };

    render() {
        const button = !this.props.isEmptyFilters() ? (
            <MDBBtn color='deep-orange' onClick={this.props.cleanFilter} style={{ marginBottom: '1rem' }}>
                Clean Filter
            </MDBBtn>
        ) : null;
        return (
            <MDBRow>
                <MDBCol style={{ maxWidth: '100%' }}>
                    <MDBCard reverse>
                        <MDBCardBody cascade className='text-center'>
                            <>
                                <MDBBtn
                                    color={this.props.categoryButtonColor}
                                    onClick={this.toggleCollapse('category')}
                                    style={{ marginBottom: '1rem' }}>
                                    Add category filter
                                </MDBBtn>
                                <MDBBtn
                                    color={this.props.dateButtonColor}
                                    onClick={this.toggleCollapse('date')}
                                    style={{ marginBottom: '1rem' }}>
                                    Add data filter
                                </MDBBtn>
                                <MDBBtn
                                    color={this.props.cityButtonColor}
                                    onClick={this.toggleCollapse('city')}
                                    style={{ marginBottom: '1rem' }}>
                                    Select City
                                </MDBBtn>

                                {button}
                                <MDBCollapse id='category' isOpen={this.props.collapseID}>
                                    <div className='container'>
                                        <CategoryList
                                            categories={this.props.categories}
                                            filterCategories={this.props.filterCategories}
                                            addFilterCategorieId={this.props.addFilterCategorieId}
                                            deleteFilterCategorieId={this.props.deleteFilterCategorieId}
                                            setButtonColor={this.props.setCategoryButtonColor}
                                        />
                                    </div>
                                </MDBCollapse>
                                <MDBCollapse id='date' isOpen={this.props.collapseID}>
                                    <div className='container'>
                                        <DatePicker
                                            setButtonColor={this.props.setDateButtonColor}
                                            setFilterDateFrom={this.props.setFilterDateFrom}
                                            setFilterDateTo={this.props.setFilterDateTo}
                                            dateFrom={this.props.dateFrom}
                                            dateTo={this.props.dateTo}
                                        />
                                    </div>
                                </MDBCollapse>
                                <MDBCollapse id='city' isOpen={this.props.collapseID}>
                                    <div className='container'>
                                        <CityInput
                                            setButtonColor={this.props.setCityButtonColor}
                                            cityName={this.props.filterCityName}
                                            setCity={this.props.setFilterCityName}
                                            showCityName={this.props.showCityName}
                                        />
                                    </div>
                                </MDBCollapse>
                            </>
                        </MDBCardBody>
                    </MDBCard>
                </MDBCol>
            </MDBRow>
        );
    }
}
