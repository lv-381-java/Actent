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
import Chat from './component/chat/Chat';

import { Route, Switch, Redirect } from 'react-router-dom';
import ReviewForm from './component/review/ReviewForm';
import ReviewList from './component/review/ReviewList';

export default class App extends React.Component {
    constructor(props) {
        super(props);
        configureAxios();
        this.state = {
            isAuthenticated: false,
        };
        this.setCurrentUser();
    }

    setCurrentUser = _ => {
        getCurrentUser()
            .then(res => {
                this.setState({
                    currentUser: res.data,
                    currentUserId: res.data.id,
                    isAuthenticated: true,
                });
            })
            .catch(e => console.error(e));
    };

    render() {
        return (
            <div>
                <Menu />
                <Switch>
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
                    <Route path='/userEvents' render={() => <UserEventsPage />} />
                    <Route path='/createEvent' render={() => <FormContainer />} />
                    <Route path='/confirm' component={Confirm} />
                    <Route path='/chat' render={() => <Chat chatId={1} />} />
                    <Route path='/addReview/:targetId' render={props => <ReviewForm {...props} />} />
                    <Route path='/reviews/:userId' render={props => <ReviewList {...props} />} />
                    <Route exact path='/' component={RenderEventFilterPage} />
                </Switch>
                <Footer />
            </div>
        );
    }
}
