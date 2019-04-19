import React from 'react';
import Title from './title/Title.jsx';
import Window from './window/Window.jsx';
import Info from './info/Info.jsx';
import Chat from './chat/Chat.jsx';
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

    // static getDerivedStateFromProps(props, state) {

    //     if (state.userId !== props.currentUserId) {
    //         state.userId = props.currentUserId;
    //     }

    //     if (state.userId !== undefined) {
    //         props.eventUserList.forEach(e => {
    //             if (e.userId == state.userId) {
    //                 if (e.eventUserType === 'PARTICIPANT') {
    //                     state.isParticipant = true;
    //                     state.isSpectator = false;
    //                     state.assID = e.id;
    //                 } else {
    //                     state.isParticipant = false;
    //                     state.isSpectator = true;
    //                     state.assID = e.id;
    //                 }
    //             }
    //         });
    //     }
    // }

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

        const button = (!this.isAssigne() || this.state.isParticipant) ? (  <div className='b-1'>

        <Participant
            setIsParticipant={this.setIsParticipant}
            setUnassigne={this.setUnassigne}
            currentUserId={this.props.currentUserId}
            eventId={this.props.eventId}
            assigne={this.state.assigne}
            assID={this.state.assID}
        />
    </div>) : ( <div className='b-2'>
                                    <Spectator
                                        setUnassigne={this.setUnassigne}
                                        setIsSpectator={this.setIsSpectator}
                                        currentUserId={this.props.currentUserId}
                                        eventId={this.props.eventId}
                                        assigne={this.state.isSpectator}
                                        assID={this.state.assID}
                                    />
                                </div>);
        return (
            <div className='container-fluid'>
                <div className='row'>
                    <div className='col-md-9'>
                        <div className='card-title'>
                            <Title title={this.props.title} />
                        </div>

                        <div className='but'>
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

                        <div className='card-chat box'>
                            <Chat chat={this.props.chat} />
                        </div>
                        <Subscribe addSubscribe={this.props.addSubscribe} />
                    </div>
                </div>
            </div>
        );
    }
}

export default Show;
