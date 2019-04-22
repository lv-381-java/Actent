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
    };

    setSelectTab = tabId => {
        this.setState({selectTab: tabId}, () => this.getEvents());
    };

    setLocation = address => {
        this.setState({filterAddress: address}, () => this.getEvents());
    };

    setUserType = userType => {
        console.log(userType);
        this.setState({filterUserType: userType}, () => this.getEvents());
    };

    setCategory = categoryName => {
        this.setState({filterCategory: categoryName}, () => this.getEvents());
    };

    getEvents() {
        let userId = this.props.match.params.userId;
        if (userId === undefined) {
            return
        }
        let url = '/eventsUsers/';
        if (this.state.selectTab === 0) {
            url += `allEvents/${userId}`;
        } else if (this.state.selectTab === 1) {
            url += `futureEvents/${userId}`;
        } else if (this.state.selectTab === 2) {
            url += `pastEvents/${userId}`;
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

        console.log(url);
        axios
            .get(url)
            .then(response => {
                const events = response.data;
                this.setState({events: events});
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    componentDidMount() {
        this.getEvents();
    }

    render() {
        return (
            <div style={style}>
                <div>
                    <PageTitle/>
                    <MainFilter setLocation={this.setLocation} setUserType={this.setUserType}
                                setCategory={this.setCategory}/>
                    <TabContainer setSelectTab={this.setSelectTab} selectTab={this.state.selectTab}
                                  events={this.state.events}/>
                </div>
            </div>
        );
    }
}
