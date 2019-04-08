import React from 'react';
import Event from './Event';

export default class PresentEventList extends React.Component {
    render() {
        return (
            <div>
                {this.props.events.map(event => (
                    <Event key={event.eventId.toString()} eventId={event.eventId} title={event.eventTitle}
                           userId={event.userId}
                           description={event.eventDescription}/>
                ))}
            </div>
        );
    }
}
