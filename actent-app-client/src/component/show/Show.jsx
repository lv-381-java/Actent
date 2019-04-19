import React from 'react';
import Title from './title/Title.jsx';
import Window from './window/Window.jsx';
import Info from './info/Info.jsx';
import Chat from '../chat/Chat';
import './Show.css'
import Participant from './button/Participant.jsx';
import Spectator from './button/Spectator.jsx';
import Subscribe from './button/Subscribe';

class Show extends React.Component {

    state = {
        userId: undefined,
        isParticipant: false,
        isSpectator: false,
        assID: undefined,
    };

    isAssigne = () => {
        return this.props.isParticipant || this.props.isSpectator;
    };

    setIsParticipant = () => {
        this.setState({
            isParticipant: true,
            isSpectator: false,
        });
    };

    setIsSpectator = () => {
        this.setState({
            isParticipant: false,
            isSpectator: true,
        });
    };

    setUnassigne = () => {
        this.setState({
            isParticipant: false,
            isSpectator: false,
        });
    };

    render() {

        let button = undefined;

        if (!this.isAssigne()) {

            button = (
                <div className='row'>
  
                    <Participant
                        setIsParticipant={this.setIsParticipant}
                        setUnassigne={this.setUnassigne}
                        currentUserId={this.props.currentUserId}
                        eventId={this.props.eventId}
                        assigne={this.props.isParticipant}
                        assID={this.props.assID}
                    />

                    <Spectator
                        setUnassigne={this.setUnassigne}
                        setIsSpectator={this.setIsSpectator}
                        currentUserId={this.props.currentUserId}
                        eventId={this.props.eventId}
                        assigne={this.props.isSpectator}
                        assID={this.props.assID}
                    />

                </div>)

        } else if (this.props.isParticipant) {

            button = (   
                <div className='row'>
                    <Participant
                        setIsParticipant={this.setIsParticipant}
                        setUnassigne={this.setUnassigne}
                        currentUserId={this.props.currentUserId}
                        eventId={this.props.eventId}
                        assigne={this.props.isParticipant}
                        assID={this.props.assID}
                    />
                </div>)

        } else {

            button = (   
                <div className='row'>
                    <Spectator
                        setUnassigne={this.setUnassigne}
                        setIsSpectator={this.setIsSpectator}
                        currentUserId={this.props.currentUserId}
                        eventId={this.props.eventId}
                        assigne={this.props.isSpectator}
                        assID={this.props.assID}
                    />
                </div>)
        }

        return (
            <div className='container-fluid'>
                <div className='row'>
                    <div className='col-md-9'>
                        <div className='card-title'>
                            <Title title={this.props.title} />
                        </div>

                        <div className='col-md-6'>
                            {button}
                        </div>

                        <div className='card-window'>
                            <Window
                                description={this.props.description}
                                image={this.props.image}
                                equipments={this.props.equipments}
                                reviews={this.props.reviews}
                                creatorId={this.props.creatorId}
                                eventId={this.props.eventId}
                            />
                        </div>
                    </div>

                    <div className='col-md-3'>
                        <div className='card-info box'>
                            <Info
                                info={this.props.info}
                                creationDate={this.props.creationDate}
                                startDate={this.props.startDate}
                                duration={this.props.duration}
                                capacity={this.props.capacity}
                                category={this.props.category}
                                creatorFirstName={this.props.creatorFirstName}
                                creatorLastName={this.props.creatorLastName}
                                participants={this.props.participants}
                                spectators={this.props.spectators}
                            />
                        </div>
                            {console.log(this.props.chat)}
                        <div>
                            {this.props.chat != undefined ? <Chat chatId={this.props.chat} /> : console.log("waiting for chat id")}
                        </div>
                        <Subscribe addSubscribe={this.props.addSubscribe} />
                    </div>
                </div>
            </div>
        );
    }
}

export default Show;
