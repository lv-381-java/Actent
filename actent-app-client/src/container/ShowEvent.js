import React from 'react';
import Show from '../component/show/Show.jsx';
import Axios from 'axios';

class ShowEvent extends React.Component {
    state = {
        eventId: Number(this.props.match.params.id),
        reviews: ['first review', 'second review', 'third review', 'fff review', 'fourth review', 'fifth review', 'sixth review', 'seventh review', 'eight review', 'ten review', 'eleven review', 'twelve review', 'thirteen review', 'fourteen review', '15 review', '16 review', '17 review', '18 review', '19 review', '20 review', '21 review', '22 review', '23 review', '24 review', '25 review', '26 review', '27 review', '28 review', '29 review'],
        creatorId: undefined,
        participants: undefined,
        spectators: undefined,
        eventUserList: [],
    };

    componentDidMount() {
        this.getEvent();
        this.getParticipants();
    }

    getEvent = () => {
        Axios.get(`http://localhost:8080/api/v1/events/${this.state.eventId}`)
            .then(eve => {
                this.setState({
                    title: eve.data['title'],
                    description: eve.data['description'],

                    image: eve.data['image'],
                    chat: eve.data.Chat.id,

                    equipments: eve.data['equipments'],
                    //reviews: eve.data.feedback,

                    creationDate: eve.data.creationDate,
                    startDate: eve.data['startDate'],
                    duration: eve.data['duration'],
                    capacity: eve.data['capacity'],
                    category: eve.data.Category.name,
                    creatorId: eve.data.Creator.id,
                    creatorFirstName: eve.data.Creator.firstName,
                    creatorLastName: eve.data.Creator.lastName,
                });
            })
            .catch(function(error) {
                console.log(error);
            });
    };

    getParticipants = () => {
        Axios.get(`http://localhost:8080/api/v1/eventsUsers/events/${this.state.eventId}`).then(part => {
            let participantsCount = 0;
            let spectatorsCount = 0;

            part.data.forEach(e => {
                e.eventUserType === 'PARTICIPANT' ? participantsCount++ : spectatorsCount++;
            });

            this.setState({
                ...this.state,
                participants: participantsCount,
                spectators: spectatorsCount,
                eventUserList: part.data,
            });
        });
    };

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
                />
            </div>
        );
    }
}

export default ShowEvent;
