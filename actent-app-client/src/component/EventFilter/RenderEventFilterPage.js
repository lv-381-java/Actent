import React from 'react';
import Header from './Header';
import FilterBody from './FilterBody';
import CardExample from './Cart';
import { BrowserRouter } from 'react-router-dom';
import axios from 'axios';
import Pagination from 'react-js-pagination';
import './pagination.css';
import GoogleButton from 'react-google-button';
import { GOOGLE_AUTH_URL, FACEBOOK_AUTH_URL, GITHUB_AUTH_URL, ACCESS_TOKEN } from '../../constants/apiConstants';
const cartStyle = {
    paddingTop: '2rem',
};

export default class RenderEventFilterPage extends React.Component {
    state = {
        filterTitle: '',
        showTitleName: '',

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

        collapseID: '',
        checked: undefined,
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
                showTitleName: '',
                dateButtonColor: 'info',
                cityButtonColor: 'info',
                categoryButtonColor: 'info',
                collapseID: '',
                checked: false,
            },

            () => this.getEvents(),
        );
        this.setCityButtonColor('info');
        this.setCategoryButtonColor('info');
        this.setDateButtonColor('info');
    };

    setFilterTitle = (title, keyCode) => {
        if (keyCode === 13) {
            this.setState({ filterTitle: title, showTitleName: title }, () => this.eventsFilter());
        } else {
            this.setState({ showTitleName: title });
        }
    };

    addFilterCategorieId = categoriesId => {
        let filterCategories = this.state.filterCategories;
        filterCategories.push(categoriesId);
        this.setState({ filterCategories: filterCategories }, () => this.eventsFilter());
        this.setCategoryButtonColor('success');
    };

    deleteFilterCategorieId = categoriesId => {
        let filterCategories = this.state.filterCategories.filter(id => id !== categoriesId);
        this.setState({ filterCategories: filterCategories }, () => this.eventsFilter());
        if (filterCategories.length === 0) {
            this.setState({ categoryButtonColor: 'info' });
        } else {
            this.setState({ categoryButtonColor: 'success' });
        }
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
            this.setState({ filterCityName: cityName, showCityName: cityName, cityButtonColor: 'success' }, () =>
                this.eventsFilter(),
            );
        } else {
            this.setState({ showCityName: cityName }, () => this.setCityButtonColor('info'));
        }
        console.log(this.state.showCityName);
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
    setCollapseID = name => {
        this.setState({ collapseID: name });
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
        this.setState({ activePage: pageNumber }, () => this.getEvents());
    };

    eventsFilter = () => {
        const data = {
            categoriesId: this.state.filterCategories,
            locationAddress: this.state.filterCityName,
            dateFrom: this.state.filterDateFrom,
            dateTo: this.state.filterDateTo,
            title: this.state.filterTitle,
        };
        if (data.title.length === 0 && data.locationAddress.length === 0 && data.categoriesId.length === 0) {
            this.getEvents();
        } else {
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
        }
    };

    getEvents = () => {
        let page = this.state.activePage - 1;
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
                    <Header setTitle={this.setFilterTitle} showTitleName={this.state.showTitleName} />
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
                        collapseID={this.state.collapseID}
                        setCollapseID={this.setCollapseID}
                        checked={this.state.checked}
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
                                        city={event.Location.address}
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
