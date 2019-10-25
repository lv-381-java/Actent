import React from 'react';
import Show from '../component/show/Show.jsx';
import Axios from 'axios';
import {getCurrentUser} from '../util/apiUtils';
import {API_BASE_URL} from "../constants/apiConstants";

class ShowEvent extends React.Component {
    
    state = {
        eventId: Number(this.props.match.params.id),
        reviews: [],
        creatorId: undefined,
        participants: undefined,
        spectators: undefined,
        eventUserList: [],
        userId: undefined,
        isParticipant: false,
        isSpectator: false,
        assID: undefined,
    };

    componentDidMount() {
        this.getEvent();
        this.getParticipants();
        this.setButtonsState();
    }

    setButtonsState() {
        if (this.state.userId) {
            this.state.eventUserList.map(e => {
                if (e.userId == this.state.userId) {
                    if (e.eventUserType === 'PARTICIPANT') {
                        this.setState({
                            isParticipant: true,
                            isSpectator: false,
                            assID:e.id
                        });
                    } else {
                        this.setState({
                            isParticipant: false,
                            isSpectator: true,
                            assID: e.id,
                        })
                    }
                }
            });
        }
    }

    setCurrentUserId = () => {
        getCurrentUser().then(
            res => {
                this.setState({userId: res.data.id}, ()=>this.setButtonsState())
            }).catch(e => {
            console.log(e)
        });
    };

    addSubscribe = () => {
        const data = { category: this.state.category, city: this.state.location };
        Axios.post(`/subscribers`, data)
            .catch(function(error) {
                console.error(error);
            });
    };

    getEvent = () => {
        Axios.get(API_BASE_URL + `/events/${this.state.eventId}`)
            .then(eve => {
                this.setState({
                    title: eve.data['title'],
                    description: eve.data['description'],
                    image: eve.data['image'],
                    chat: eve.data.Chat.id,
                    equipments: eve.data['equipments'],
                    creationDate: eve.data.creationDate,
                    startDate: eve.data['startDate'],
                    duration: eve.data['duration'],
                    capacity: eve.data['capacity'],
                    category: eve.data.Category.name,
                    creatorId: eve.data.Creator.id,
                    creatorFirstName: eve.data.Creator.firstName,
                    creatorLastName: eve.data.Creator.lastName,
                    location: eve.data.Location.address,
                });
            })
            .catch(function(error) {
                console.log(error);
            });
    };

    getParticipants = () => {
        Axios.get(API_BASE_URL + `/eventsUsers/events/${this.state.eventId}`).then(res => {
          let eventUserList = res.data;
          this.setState({ eventUserList }, () => this.setCurrentUserId());
        });
    };

    setParticipantFromButton = param => {
        this.setState({ isParticipant: param })
    }

    setSpectatorFromButton = param => {
        this.setState({ isSpectator: param })
    }

    setAssigneIdInButton = id => {
        this.setState({ assID: id })
    }

    render() {
        return (
            <div>
                <Show
                    title={this.state.title}
                    description={this.state.description}
                    image={this.state.image}
                    info={this.state.info}
                    chat={this.state.chat}
                    equipments={this.state.equipments}
                    reviews={this.state.reviews}
                    creationDate={this.state.creationDate}
                    startDate={this.state.startDate}
                    duration={this.state.duration}
                    capacity={this.state.capacity}
                    category={this.state.category}
                    creatorFirstName={this.state.creatorFirstName}
                    creatorLastName={this.state.creatorLastName}
                    creatorId={this.state.creatorId}
                    participants={this.state.participants}
                    spectators={this.state.spectators}
                    eventId={this.state.eventId}
                    eventUserList={this.state.eventUserList}
                    currentUserId={this.state.userId}
                    addSubscribe={this.addSubscribe}
                    isParticipant={this.state.isParticipant}
                    isSpectator={this.state.isSpectator}
                    assID={this.state.assID}
                    setParticipantFromButton={this.setParticipantFromButton}
                    setSpectatorFromButton={this.setSpectatorFromButton}
                    setAssigneIdInButton={this.setAssigneIdInButton}
                />
            </div>
        );
    }
}

export default ShowEvent;
