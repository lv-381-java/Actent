import React from 'react';
import Title from './title/Title.jsx'
import Window from './window/Window.jsx';
import Info from './info/Info.jsx';
import Chat from './chat/Chat.jsx';
import './Show.css';
import Participant from './button/Participant.jsx';
import Spectator from './button/Spectator.jsx';
import { getCurrentUser } from '../../util/apiUtils.js';

class Show extends React.Component {

    state = {
        currentUserId: undefined,
        assigne: undefined,
    }

    async componentDidMount() {
        try {
            const data = (await getCurrentUser()).data;
            let assi = false;
            
            this.props.eventUserList.forEach(e => {
                e.userId === data.id ? assi = true : assi = false;
            });

            this.setState({
                ...this.state,
                userId: data.id,
                assigne: assi,
            });
            console.log(this.props.eventUserList)

        } catch (e) {
            console.error(e);
        }
    }

    render() {

        return(
            <div className='page'>

                <div className='container-1'>
                    <div className='card-title'>
                        <Title
                            title={this.props.title}
                        />
                    </div>

                    <div className='but'>
                        <div className='b-1'>
                            <Participant 
                                currentUserId={this.state.userId}
                                eventId={this.props.eventId}
                                assigne={this.state.assigne}
                            />
                        </div>

                        <div className='b-2'>
                            <Spectator 
                                currentUserId={this.state.userId}
                                eventId={this.props.eventId}
                                assigne={this.state.assigne}
                            />
                        </div>
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

                <div className='container-2'>
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
                        <Chat
                            chat={this.props.chat}
                        />
                    </div>
                </div>

            </div>
        );
    }
}

export default Show;
