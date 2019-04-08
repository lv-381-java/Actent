import React, { Component } from 'react';
import { MDBCard, MDBCardBody, MDBRow, MDBCol } from 'mdbreact';
import FilterButtoms from './FilterButtoms';

export default class FilterBody extends Component {
    render() {
        return (
            <MDBRow>
                <MDBCol style={{ maxWidth: '100%' }}>
                    <MDBCard reverse>
                        <MDBCardBody cascade className='text-center'>
                            <FilterButtoms
                                categoriesId={this.props.categoriesId}
                                cityName={this.props.cityName}
                                setDateRange={this.props.setDateRange}
                                cleanFilter={this.props.cleanFilter}
                                filteredEvents={this.props.filteredEvents}
                                setCategoriesId={this.props.setCategoriesId}
                                categories={this.props.categories}
                                setCity={this.props.setCity}
                            />
                        </MDBCardBody>
                    </MDBCard>
                </MDBCol>
            </MDBRow>
        );
    }
}
