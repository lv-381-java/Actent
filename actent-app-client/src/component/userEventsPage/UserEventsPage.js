import React from 'react';
import PageTitle from './components/PageTitle';
import MainFilter from './components/filters/MainFilter';
import TabContainer from './components/TabContainer';
import axios from 'axios';

const style = {
    minHeight: '700px',
};

export default class UserEventsPage extends React.Component {
    state = {
        filterAddress: undefined,
        filterUserType: undefined,
        filterCategory: undefined,
        events: [],
        selectTab: 0,
        activePage: 1,
        totalItems: 0,
        totalItemsPast: 0,
        totalItemsFuture: 0,
    };

    componentDidMount() {
        this.getEvents();
        this.getTotalItems();
        this.getTotalItemsPast();
        this.getTotalItemsFuture();
    }

    setSelectTab = tabId => {
        this.setState({selectTab: tabId}, () => this.getEvents());
    };

    setLocation = address => {
        this.setState({filterAddress: address}, () => this.getEvents());
    };

    setUserType = userType => {
        this.setState({filterUserType: userType}, () => this.getEvents());
    };

    setCategory = categoryName => {
        this.setState({filterCategory: categoryName}, () => this.getEvents());
    };

    handlePageChange = pageNumber => {
        this.setState({activePage: pageNumber}, () => this.getEvents());
    };

    getEvents() {
        let userId = this.props.match.params.userId;

        let page = this.state.activePage - 1;

        if (userId === undefined) {
            return
        }
        let url = '/eventsUsers/';
        if (this.state.selectTab === 0) {
            url += `allEvents/${userId}/${page}/4`;
        } else if (this.state.selectTab === 1) {
            url += `futureEvents/${userId}/${page}/4`;
        } else if (this.state.selectTab === 2) {
            url += `pastEvents/${userId}/${page}/4`;
        }
        if (this.state.filterCategory || this.state.filterAddress || this.state.filterUserType) {
            url += '?';
            if (this.state.filterAddress) {
                url += 'address=' + this.state.filterAddress + '&';
            }
            if (this.state.filterCategory) {
                url += 'category=' + this.state.filterCategory + '&';
            }
            if (this.state.filterUserType) {
                url += 'userType=' + this.state.filterUserType + '&';
            }
        }

        axios
            .get(url)
            .then(response => {
                const events = response.data;
                this.setState({events: events});
            })
            .catch(function (error) {
                console.error(error);
            });
    }

    getTotalItems = () => {
        let userId = this.props.match.params.userId;
        let url = `/eventsUsers/total/${userId}`;
        axios
            .get(url)
            .then(response => {
                let totalItems = response.data;
                this.setState({totalItems});
            })
            .catch(function (error) {
                console.error(error);
            });
    };
    getTotalItemsPast = () => {
        let userId = this.props.match.params.userId;
        let url = `/eventsUsers/totalPastEvents/${userId}`;
        axios
            .get(url)
            .then(response => {
                let totalItemsPast = response.data;
                this.setState({totalItemsPast});
            })
            .catch(function (error) {
                console.error(error);
            });
    };
    getTotalItemsFuture = () => {
        let userId = this.props.match.params.userId;
        let url = `/eventsUsers/totalFutureEvents/${userId}`;
        axios
            .get(url)
            .then(response => {
                let totalItemsFuture = response.data;
                this.setState({totalItemsFuture});
            })
            .catch(function (error) {
                console.error(error);
            });
    };

    render() {
        return (
            <div style={style}>
                <div>
                    <PageTitle/>
                    <MainFilter setLocation={this.setLocation} setUserType={this.setUserType}
                                setCategory={this.setCategory}/>
                    <TabContainer setSelectTab={this.setSelectTab} selectTab={this.state.selectTab}
                                  events={this.state.events} handlePageChange={this.handlePageChange}
                                  activePage={this.state.activePage} pageCount={this.state.totalItems}
                                  pageCountFuture={this.state.totalItemsFuture}
                                  pageCountPast={this.state.totalItemsPast}/>
                </div>
            </div>
        );
    }
}
