import React from 'react';
import Profile from './component/profile/Profile';
import FormContainer from './component/createevent/FormContainer';
import RenderEventFilterPage from './component/EventFilter/RenderEventFilterPage';
import UserEventsPage from './component/userEventsPage/UserEventsPage';
import ShowEvent from './container/ShowEvent';
import SignInUp from './component/SignUpInForm';
import { configureAxios, getCurrentUser } from './util/apiUtils';
import Confirm from './component/confirm/Confirm';
import Menu from './component/EventFilter/Menu';
import Footer from './component/EventFilter/Footer';

import { Route, Switch, Redirect } from 'react-router-dom';
import ReviewForm from './component/review/ReviewForm';
import ReviewList from './component/review/ReviewList';
import OAuth2RedirectHandler from './component/oauth2/OAuth2RedirectHendler';

export default class App extends React.Component {
    constructor(props) {
        super(props);
        configureAxios();
        this.state = {
            isAuthenticatedSocial: false,
            isAuthenticated: false,
        };
        this.setCurrentUser();
    }

    setCurrentUser = _ => {
        getCurrentUser()
            .then(res => {
                console.log(res.data);
                this.setState({
                    currentUserFirstName: res.data.firstName,
                    currentUser: res.data,
                    currentUserId: res.data.id,
                    isAuthenticated: true,
                });
            })
            .catch(e => console.error(e));
    };
    setSocial = e => {
        this.setState({ isAuthenticatedSocial: e });
    };

    render() {
        return (
            <div>
                <Menu userId={this.state.currentUserId} firstName={this.state.currentUserFirstName}/>
                <Switch>
                    <Route path='/home' component={RenderEventFilterPage} />
                    <Route path='/auth' component={SignInUp} />
                    <Route path='/show/:id' component={ShowEvent} />
                    <Route path='/show' render={() => <ShowEvent />} />
                    <Route
                        path='/profile'
                        render={
                            this.state.currentUserId
                                ? props => {
                                      console.log(this);

                                      return <Redirect to={`/users/${this.state.currentUserId}`} />;
                                  }
                                : console.log('Waiting for currentUserId...')
                        }
                    />
                    <Route
                        path='/users/:id'
                        render={
                            this.state.currentUserId
                                ? props => {
                                      props =
                                          Number(props.match.params.id) === Number(this.state.currentUserId)
                                              ? { ...props, current: true }
                                              : { ...props, current: false };
                                      return <Profile {...props} />;
                                  }
                                : console.log('Waiting for currentUserId...')
                        }
                    />
                    <Route path='/userEvents/:userId' render={props => <UserEventsPage {...props} />} />
                    <Route path='/createEvent' render={() => <FormContainer />} />
                    <Route path='/confirm' component={Confirm} />
                    <Route path='/addReview/:targetId' render={props => <ReviewForm {...props} />} />
                    <Route path='/reviews/:userId' render={props => <ReviewList {...props} />} />
                    <Route path='/oauth2/redirect' component={OAuth2RedirectHandler} />
                    <Route
                        render={() => {
                            return <Redirect to='/home' />;
                        }}
                    />
                </Switch>
                <Footer />
            </div>
        );
    }
}
