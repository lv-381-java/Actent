import React from 'react';
import Header from './Header';
import FilterBody from './FilterBody';
import CardExample from './Cart';
import { BrowserRouter } from 'react-router-dom';
import axios from 'axios';
import Pagination from 'react-js-pagination';
import './pagination.css';

const cartStyle = {
    paddingTop: '2rem',
};

export default class RenderEventFilterPage extends React.Component {
    state = {
        filterTitle: '',

        categories: [],
        filterCategories: [],

        filterDateFrom: undefined,
        filterDateTo: undefined,

        filterCityName: '',
        showCityName: '',

        events: [],

        activePage: 1,
        totalItems: 0,

        dateButtonColor: 'info',
        cityButtonColor: 'info',
        categoryButtonColor: 'info',
    };

    componentDidMount() {
        this.getEvents();
        this.getTottalItems();
        this.getCategories();
    }

    isEmptyFilters = () => {
        return (
            this.state.filterTitle.length === 0 &&
            this.state.filterCategories.length === 0 &&
            this.state.filterDateFrom === undefined &&
            this.state.filterDateTo === undefined &&
            this.state.filterCityName.length === 0
        );
    };

    cleanFilter = () => {
        this.setState(
            {
                filterTitle: '',
                filterCategories: [],
                filterDateFrom: undefined,
                filterDateTo: undefined,
                filterCityName: '',
                showCityName: '',
            },
            () => this.getEvents(),
        );
        this.setCityButtonColor('info');
        this.setDateButtonColor('info');
        this.setCategoryButtonColor();
    };

    setFilterTitle = title => {
        this.setState({ filterTitle: title }, () => this.eventsFilter());
    };

    addFilterCategorieId = categoriesId => {
        let filterCategories = this.state.filterCategories;
        filterCategories.push(categoriesId);
        this.setState({ filterCategories: filterCategories }, () => this.eventsFilter());
    };

    deleteFilterCategorieId = categoriesId => {
        let filterCategories = this.state.filterCategories.filter(id => id !== categoriesId);
        this.setState({ filterCategories: filterCategories }, () => this.eventsFilter());
    };

    setFilterDateFrom = dateFrom => {
        this.setState({ filterDateFrom: dateFrom }, () => this.eventsFilter());
    };

    setFilterDateTo = dateTo => {
        this.setState({ filterDateTo: dateTo }, () => this.eventsFilter());
    };

    setFilterCityName = (cityName, keyCode) => {
        console.log(keyCode);
        if (keyCode === 13) {
            this.setState({ filterCityName: cityName, showCityName: cityName, categoryButtonColor: 'success' }, () =>
                this.eventsFilter(),
            );
            this.setCategoryButtonColor();
        } else {
            this.setState({ showCityName: cityName });
        }
        console.log(this.state.showCityName);
    };

    setCategoryButtonColor = () => {
        let filterCategories = this.state.filterCategories;
        console.log(filterCategories.length);
        filterCategories.length === 0
            ? this.setState({ categoryButtonColor: 'info' })
            : this.setState({ categoryButtonColor: 'success' });
    };

    setCityButtonColor = () => {
        this.state.filterCityName === ''
            ? this.setState({ cityButtonColor: 'info' })
            : this.setState({ cityButtonColor: 'success' });
    };

    setDateButtonColor = color => {
        this.setState({ dateButtonColor: color });
    };

    getCategories = () => {
        axios
            .get(`/categories`)
            .then(res => {
                let categories = res.data;
                this.setState({ categories });
            })
            .catch(function(error) {
                console.error(error);
            });
    };

    getTottalItems = () => {
        axios
            .get(`/events/totalElements`)
            .then(res => {
                let totalItems = res.data;
                this.setState({ totalItems }, () => console.log(this.state.totalItems));
            })
            .catch(function(error) {
                console.error(error);
            });
    };

    handlePageChange = pageNumber => {
        console.log(`active page is ${pageNumber}`);
        this.setState({ activePage: pageNumber }, () => this.getEvents());
    };

    eventsFilter = () => {
        const data = {
            categoriesId: this.state.filterCategories,
            cityName: this.state.filterCityName,
            dateFrom: this.state.filterDateFrom,
            dateTo: this.state.filterDateTo,
            title: this.state.filterTitle,
        };

        axios
            .post(`/events/filter`, data)
            .then(res => {
                let events = res.data;
                this.setState({
                    events: events,
                });
            })
            .catch(function(error) {
                console.error(error);
            });
    };

    getEvents = () => {
        let page = this.state.activePage - 1;
        console.log('age');
        console.log(page);
        axios
            .get(`/events/all/${page}/9`)
            .then(res => {
                let events = res.data;
                this.setState({ events: events });
            })
            .catch(function(error) {
                console.error(error);
            });
    };

    render() {
        let events = this.state.events;
        return (
            <div>
                <BrowserRouter>
                    <Header setTitle={this.setFilterTitle} />
                </BrowserRouter>
                <div className='container'>
                    <FilterBody
                        isEmptyFilters={this.isEmptyFilters}
                        cleanFilter={this.cleanFilter}
                        categories={this.state.categories}
                        filterCategories={this.state.filterCategories}
                        addFilterCategorieId={this.addFilterCategorieId}
                        deleteFilterCategorieId={this.deleteFilterCategorieId}
                        setFilterDateFrom={this.setFilterDateFrom}
                        setFilterDateTo={this.setFilterDateTo}
                        filterCityName={this.state.filterCityName}
                        showCityName={this.state.showCityName}
                        setFilterCityName={this.setFilterCityName}
                        dateFrom={this.state.filterDateFrom}
                        dateTo={this.state.filterDateTo}
                        dateButtonColor={this.state.dateButtonColor}
                        cityButtonColor={this.state.cityButtonColor}
                        categoryButtonColor={this.state.categoryButtonColor}
                        setCategoryButtonColor={this.setCategoryButtonColor}
                        setCityButtonColor={this.setCityButtonColor}
                        setDateButtonColor={this.setDateButtonColor}
                    />
                    <div className='row'>
                        {events.map(event => {
                            return (
                                <div
                                    key={event.id}
                                    className='col-md-4 col-sm-12 align-self-center cart'
                                    style={cartStyle}>
                                    <CardExample
                                        title={event.title}
                                        eventId={event.id}
                                        description={event.description}
                                        city={event.Location.Country.Region.City.name}
                                        category={event.Category.name}
                                    />
                                </div>
                            );
                        })}
                    </div>
                    <div className='row' style={{ margin: 'auto', marginTop: '50px' }}>
                        <nav aria-label='Page navigation example'>
                            <Pagination
                                activePage={this.state.activePage}
                                itemsCountPerPage={9}
                                totalItemsCount={this.state.totalItems}
                                pageRangeDisplayed={5}
                                onChange={this.handlePageChange}
                                innerClass={'pagination'}
                                itemClass={'page-item'}
                                linkClass={'page-link pagination-border'}
                            />
                        </nav>
                    </div>
                </div>
            </div>
        );
    }
}
